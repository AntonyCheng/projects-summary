package top.sharehome.proapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import top.sharehome.proapiclientsdk.model.User;
import top.sharehome.proapiclientsdk.utils.SignUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 调用第三方接口的客户端
 * @Author:AntonyCheng
 * @CreateTime:2023/1/16 11:20
 */

public class ProApiClient {
    private String accessKey;
    private String secretKey;
    public static final String GATEWAY_HOST = "http://localhost:8082";

    public ProApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public ProApiClient() {
    }

    public String getNameByGet(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        return HttpUtil.get(GATEWAY_HOST + "/api/name/", paramMap);
    }

    public String getNameByPost(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        return HttpUtil.post(GATEWAY_HOST + "/api/name/", paramMap);
    }

    private Map<String, String> getHeaderMap(String body) {
        HashMap<String, String> map = new HashMap<>();
        map.put("accessKey", accessKey);
        //密钥一定不能通过网络以明文发送给后端，因为一旦被拦截就会有安全风险
        //map.put("secretKey", secretKey);
        map.put("nonce", RandomUtil.randomNumbers(4));
        map.put("body", body);
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("sign", SignUtils.getSign(body, secretKey));
        return map;
    }

    public String getUserNameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/name/user")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        return httpResponse.body();
    }
}
