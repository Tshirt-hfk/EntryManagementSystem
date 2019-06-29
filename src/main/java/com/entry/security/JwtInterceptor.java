package com.entry.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.entry.dto.BaseResultFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        // System.out.println(request.getRequestURL());
        try{
            if(request.getMethod().equals("OPTIONS")){
                return true;
            }else{
                String token = request.getHeader("Authorization");
                if (token == null){
                    response.setContentType("text/json;charset=utf-8");
                    response.setHeader("Access-Control-Allow-Origin","*");
                    String result_json = new ObjectMapper().writeValueAsString(BaseResultFactory.build(0,"用户未登录"));
                    response.getWriter().append(result_json);
                    return false;
                }
                Claims claims = JwtUtil.checkToken(token);
                request.setAttribute("userId",Integer.parseInt(claims.getSubject()));
                return true;
            }
        }catch (HttpClientErrorException e){
            response.setContentType("text/json;charset=utf-8");
            response.setHeader("Access-Control-Allow-Origin","*");
            String result_json=new ObjectMapper().writeValueAsString(BaseResultFactory.build(0,"用户校验失败，请重新登陆！"));
            response.getWriter().append(result_json);
            return false;
        }
    }
}
