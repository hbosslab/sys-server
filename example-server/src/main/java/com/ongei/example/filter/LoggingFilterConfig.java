package com.ongei.example.filter;

import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

@Configuration
public class LoggingFilterConfig implements Filter {

    private static final Logger logger = getLogger(LoggingFilterConfig.class);
    @Bean
    public FilterRegistrationBean registrationBean(){
        FilterRegistrationBean filter = new FilterRegistrationBean(new LoggingFilterConfig());
        filter.addUrlPatterns("/*");
        //多个过滤器时执行顺序
        // 最高级别。
        filter.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
        return filter;
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        // TODO Auto-generated method stub
        HttpServletRequest req = (HttpServletRequest)request;
        Map<String, Object> map = new HashMap<String, Object>();

        // Get request URL.
        map.put("URL", req.getRequestURL());
        map.put("Method", req.getMethod());
        map.put("Protocol",req.getProtocol());
        // 获取header信息

        List<Map<String,String>> headerList = new ArrayList<>();
        Map<String,String> headerMaps = new HashMap<String,String>();
        for(Enumeration<String> enu = req.getHeaderNames(); enu.hasMoreElements();){
            String name = enu.nextElement();
            headerMaps.put(name,req.getHeader(name));
        }
        headerList.add(headerMaps);
        map.put("headers", headerList);
        //获取parameters信息

        List<Map<String,String>> parameterList = new ArrayList<>();
        Map<String,String> parameterMaps = new HashMap<String,String>();
        for(Enumeration<String> names = req.getParameterNames();names.hasMoreElements();){
            String name = names.nextElement();
            parameterMaps.put(name, req.getParameter(name));
        }
        parameterList.add(parameterMaps);
        map.put("parameters", parameterList);
        String line = "";
        // 获取请求体信息
//            if (req.getMethod().equalsIgnoreCase("POST")) {
//                int len = req.getContentLength();
//                char[] buf = new char[len];
//                int bufcount = requestWrapper.getReader().read(buf);
//                if (len > 0 && bufcount <= len) {
//                    line = String.copyValueOf(buf).substring(0, bufcount);
//                }
//            } else if (req.getMethod().equalsIgnoreCase("GET")) {
        int idx = req.getRequestURL().indexOf("?");
        if (idx != -1) {
            line = req.getRequestURL().substring(idx + 1);
        } else {
            line = null;
        }
//            }

        if (line != null) {
            map.put("Context", new String[] { line });
        }
        logger.info("接收请求报文：\n"+ JSONUtil.toJsonStr(map));
        chain.doFilter(request, response);
        // 辞书
        logger.info("接收response报文：\n"+ response.getContentType());
    }

    @Override
    public void destroy() {

    }

}