package com.entry.security;

import com.entry.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.entry.dto.BaseResultFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class JwtInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LogManager.getLogger(JwtInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        try{
            String uri = request.getRequestURI();
            String medthod = request.getMethod();
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
                Integer userId = Integer.parseInt(claims.getSubject());
                request.setAttribute("userId",Integer.parseInt(claims.getSubject()));
                logger.info("user {} {} {}",userId,medthod,uri);
                return true;
            }
        }catch (HttpClientErrorException e){
            logger.warn(e);
            response.setContentType("text/json;charset=utf-8");
            response.setHeader("Access-Control-Allow-Origin","*");
            String result_json=new ObjectMapper().writeValueAsString(BaseResultFactory.build(0,"用户校验失败，请重新登陆！"));
            response.getWriter().append(result_json);
            return false;
        }
    }
}
