package com.example.authservice.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.authservice.service.RateLimitService;
import com.example.authservice.util.IpUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitInterceptor implements HandlerInterceptor{
    
    @Autowired
    private RateLimitService rateLimitService;

    @Autowired
    private IpUtil ipUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ipAddress = ipUtil.getClientIp(request);
        boolean allowed = rateLimitService.attemptRequest(ipAddress);
        if(!allowed) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many login attempts. Please try again later.");
            return false;
        }

        return true;
    }

}
