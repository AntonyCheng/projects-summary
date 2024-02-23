package top.sharehome.reggie.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import top.sharehome.reggie.common.BaseContext;
import top.sharehome.reggie.common.R;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经完成登录
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * 路径匹配，检查本次请求是否需要放行
     *
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Long loginUserId = (Long) request.getSession().getAttribute("loginUserId");
        Long userId = (Long) request.getSession().getAttribute("userId");

        //1、获取本次请求的URI
        String requestUri = request.getRequestURI();

        LoginCheckFilter.log.info("拦截到请求：{}", requestUri);

        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };

        //2、判断本次请求是否需要处理
        boolean check = check(urls, requestUri);

        //3、如果不需要处理，则直接放行
        if (check) {
            LoginCheckFilter.log.info("本次请求{}不需要处理", requestUri);
            filterChain.doFilter(request, response);
            return;
        }

        //4、判断登录状态，如果已登录，则直接放行
        if (loginUserId != null) {
            BaseContext.setCurrentId(loginUserId);
            LoginCheckFilter.log.info("用户已登录，用户id为：{}", log);
            filterChain.doFilter(request, response);
            return;
        }
        if (userId != null) {
            BaseContext.setCurrentId(userId);
            LoginCheckFilter.log.info("用户已登录，用户id为：{}", log);
            filterChain.doFilter(request, response);
            return;
        }

        LoginCheckFilter.log.info("用户未登录");
        //5、如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }
}