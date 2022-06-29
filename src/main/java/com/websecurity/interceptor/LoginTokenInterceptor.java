package com.websecurity.interceptor;


import com.websecurity.annotation.NeedToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginTokenInterceptor implements HandlerInterceptor {

    private static final String TOKEN_KEY = "token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断访问的方法上是否有@needToken注解
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            boolean needToken = handlerMethod.hasMethodAnnotation(NeedToken.class);
            // 尝试从请求头从获取token
            String token = request.getHeader(TOKEN_KEY);
            if (StringUtils.isEmpty(token)) {
                // 尝试从请求参数中获取token
                token = request.getParameter(TOKEN_KEY);
            }
            if (StringUtils.isEmpty(token)) {
                // 尝试从cookie获取token
                if (request.getCookies() != null && request.getCookies().length != 0) {
                    for (Cookie cookie : request.getCookies()) {
                        if (TOKEN_KEY.equals(cookie.getName())) {
                            token = cookie.getValue();
                        }
                    }
                }
            }
            // 若token为空,且需要token
            if (StringUtils.isEmpty(token)) {
                if (needToken) {
                    throw new RuntimeException("无token,无权访问");
                }
            }
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
