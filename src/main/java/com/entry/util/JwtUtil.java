package com.entry.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;

public class JwtUtil {

    final static String base64EncodedSecretKey = "base64EncodedSecretKey"; //私钥

    final static long TOKEN_EXP = 1000 * 60 * 60 * 24; //过期时间,测试使用60分钟

    public static String getToken(Integer id,Integer role) {
        return Jwts.builder()
                .setSubject(id.toString())
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXP)) /*过期时间*/
                .signWith(SignatureAlgorithm.HS256, base64EncodedSecretKey)
                .compact();
    }

    public static Integer getIdbyToken(String token){
        Claims claims=Jwts.parser()
                .setSigningKey(base64EncodedSecretKey)
                .parseClaimsJws(token)
                .getBody();
        return  Integer.parseInt(claims.getSubject());
    }

    public static Integer getRolebyToken(String token){
        Claims claims=Jwts.parser()
                .setSigningKey(base64EncodedSecretKey)
                .parseClaimsJws(token)
                .getBody();
        return  Integer.parseInt(claims.get("role").toString());
    }

    public static Claims checkToken(String token) {
        try {
            final Claims claims = Jwts.parser().setSigningKey(base64EncodedSecretKey).parseClaimsJws(token).getBody();
            return claims;
        }catch (ExpiredJwtException e1) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"出错了");
        } catch (Exception e) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"出错了");
        }
    }

}
