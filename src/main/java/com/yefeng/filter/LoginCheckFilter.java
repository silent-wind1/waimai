package com.yefeng.filter;

import com.alibaba.fastjson.JSON;
import com.sun.deploy.net.HttpResponse;
import com.yefeng.common.BaseContext;
import com.yefeng.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    // 路径匹配器，支持通配符
    public static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("");
        //1.获取request中的方法,将req和resp进行强转
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //2.获取用户访问的路径
        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}",requestURI);
        String[] urls = {
                "/backend/**",
                "/front/**",
                "/employee/login",
                "/employee/logout",
                "/employee/page",
                "/common/**",
                "/dish/**",
        };
        //3.判断用户访问的路径是否在放行的路径中,若是直接放行
        boolean flag = checkURI(urls, requestURI);
        if (flag) {
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        //4.若是需要拦截的路径，再获取session中登陆对象
        if(request.getSession().getAttribute("employee") != null) {
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));
            // 获取当前线程id唯一标识
            long id = Thread.currentThread().getId() ;
            log.info("线程id:{}" ,id);
            Long empId= (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request, response);
            return;
        }

        //5.若没有对象,返回R.error("NOTLOGIN")

        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    private boolean checkURI(String[] urls, String requestURI) {
        for (String url : urls) {
            //若当前请求的路径和url能够匹配上就放行
            if (antPathMatcher.match(url, requestURI)) {
                return true;
            }
        }
        return false;
    }
}
