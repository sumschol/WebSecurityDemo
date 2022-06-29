package com.websecurity.filter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Slf4j
@WebFilter(urlPatterns="/sql/*")
public class SqlInjectFilter implements Filter {
    private static final String SQL_REG_EXP = ".*(\\b(select|insert|into|update|delete|from|where|and|or|trancate" +
            "|drop|execute|like|grant|use|union|order|by)\\b).*";
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("do filter");
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        CustomRequestWrapper requestWrapper = new CustomRequestWrapper(request);
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap = getParameterMap(parameterMap, request, requestWrapper);
        // 正则校验是否有SQL关键字
        for (Object obj : parameterMap.entrySet()) {
            Map.Entry entry = (Map.Entry) obj;
            Object value = entry.getValue();
            if (value != null) {
                boolean isValid = isSqlInject(value.toString(), servletResponse);
                if (!isValid) {
                    return;
                }
            }
        }
        filterChain.doFilter(requestWrapper, servletResponse);
    }
    private Map<String, Object> getParameterMap(Map<String, Object> paramMap, HttpServletRequest request, CustomRequestWrapper requestWrapper) {
        // 1.POST请求获取参数
        if ("POST".equals(request.getMethod().toUpperCase())) {
            String body = requestWrapper.getBody();
            StringBuilder sb = new StringBuilder();
            sb.append(sb);
            sb.insert(sb.indexOf("="), '"');
            sb.insert(sb.indexOf("=") + 2, '"');
            sb.replace(sb.indexOf("="), sb.indexOf("="), ":");
            sb.insert(0, "{");
            sb.append("}");
            body = sb.toString();
            paramMap = JSONObject.parseObject(body, HashMap.class);
        } else {
            Map<String, String[]> parameterMap = requestWrapper.getParameterMap();
            //普通的GET请求
            if (parameterMap != null && parameterMap.size() > 0) {
                Set<Map.Entry<String, String[]>> entries = parameterMap.entrySet();
                for (Map.Entry<String, String[]> next : entries) {
                    paramMap.put(next.getKey(), next.getValue()[0]);
                }
            } else {
                //GET请求,参数在URL路径型式,比如server/{var1}/{var2}
                String afterDecodeUrl = null;
                try {
                    //编码过URL需解码解码还原字符
                    afterDecodeUrl = URLDecoder.decode(request.getRequestURI(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                paramMap.put("pathVar", afterDecodeUrl);
            }
        }
        return paramMap;
    }
    private boolean isSqlInject(String value, ServletResponse servletResponse) throws IOException {
        if (null != value && value.toLowerCase().matches(SQL_REG_EXP)) {
            log.info("入参中有非法字符: " + value);
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            Map<String, String> responseMap = new HashMap<>();
            // 匹配到非法字符,立即返回
            responseMap.put("code", "999");
            responseMap.put("message","入参中有非法字符");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(JSON.toJSONString(responseMap));
            response.getWriter().flush();
            response.getWriter().close();
            return false;
        }
        return true;
    }
}