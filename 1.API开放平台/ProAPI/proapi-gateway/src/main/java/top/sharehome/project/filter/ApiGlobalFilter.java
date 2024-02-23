package top.sharehome.project.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.sharehome.proapiclientsdk.utils.SignUtils;
import top.sharehome.procommon.model.InterfaceInfo;
import top.sharehome.procommon.model.User;
import top.sharehome.procommon.service.InnerInterfaceInfoService;
import top.sharehome.procommon.service.InnerUserInterfaceInfoService;
import top.sharehome.procommon.service.InnerUserService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 全局过滤器
 */
@Slf4j
@Component
public class ApiGlobalFilter implements GlobalFilter, Ordered {
    @DubboReference
    private InnerUserService innerUserService;
    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;
    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;
    public static final List<String> IP_BLACK_LIST = Arrays.asList("1.1.1.1");

    public static final String API_HOST = "http://localhost:8081";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 请求日志
        log.info("请求唯一标识：" + request.getId());
        String path = request.getPath().value();
        log.info("请求路径：" + path);
        String method = request.getMethod().toString();
        log.info("请求方法：" + method);
        log.info("请求参数：" + request.getQueryParams());
        String hostString = request.getRemoteAddress().getHostString();
        log.info("请求来源地址：" + hostString);

        // 访问控制 ==> 黑名单 这个黑名单可以进行业务逻辑上的拓展：
        // TODO 黑名单可以在网关启动时从后台数据库进行读取，由List进行封装
        // 如果白名单中不包含这个地址，那么我就直接拒绝访问
        if (IP_BLACK_LIST.contains(hostString)) {
            return handleNoAuth(response);
        }

        // 用户鉴权
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        // 实际情况应该是去数据库中查询accessKey是否分配给用户
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("getInvokeUser error,", e);
        }
        if (invokeUser == null) {
            return handleNoAuth(response);
        }
        // TODO 随机数校验，后期写这个功能
        if (Long.parseLong(nonce) > 10000) {
            return handleNoAuth(response);
        }
        // 时间和当前时间不能超过五分钟
        Long currentTime = System.currentTimeMillis() / 1000;
        final Long FIVE_MINUTES = 60 * 5L;
        if ((currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES) {
            return handleNoAuth(response);
        }
        // 从数据库中查询secretKey
        String secretKey = invokeUser.getSecretKey();
        String signBackend = SignUtils.getSign(body, secretKey);
        if (sign == null || !sign.equals(signBackend)) {
            return handleNoAuth(response);
        }
        // 判断请求模拟接口是否存在
        // 从数据库中查询模拟接口是否存在，以及请求方法是否匹配（还可以校验请求参数）
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(API_HOST + path, method);
        } catch (Exception e) {
            log.error("getInterfaceInfo error,", e);
        }
        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }
        // 校验用户是否还有调用次数
        boolean resOfInvoke = innerUserInterfaceInfoService.queryInvokeAble(interfaceInfo.getId(), invokeUser.getId());
        if (!resOfInvoke) {
            return handleNoAuth(response);
        }
        // 响应日志
        return handleResponse(exchange, chain, interfaceInfo.getId(), invokeUser.getId());
    }

    /**
     * 处理响应，并且进行调用次数的改变
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰：增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 此处的方法是再是要等完成转发之后在执行的方法
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值中写数据，拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        // 接口调用次数+1
                                        try {
                                            innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                        } catch (Exception e) {
                                            log.error("invokeCount error,", e);
                                        }
                                        // 准备打印日志
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        sb2.append("<--- {} {} \n");
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        //rspArgs.add(requestUrl);
                                        String data = new String(content, StandardCharsets.UTF_8);//data
                                        sb2.append(data);
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 调用失败的处理报错
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            //降级处理返回数据
            return chain.filter(exchange);
        } catch (Exception e) {
            log.error("网关处理响应异常\n" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
}