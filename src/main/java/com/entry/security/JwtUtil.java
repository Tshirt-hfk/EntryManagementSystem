package com.entry.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;

public class JwtUtil {
    final static String base64EncodedSecretKey = "base64EncodedSecretKey";//私钥
    final static long TOKEN_EXP = 1000 * 60 * 10;//过期时间,测试使用十分钟
    public static String getToken(Integer id,String role) {
        return Jwts.builder()
                .setSubject(id.toString())
                .claim("roles", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXP)) /*过期时间*/
                .signWith(SignatureAlgorithm.HS256, base64EncodedSecretKey)
                .compact();
    }
    public static String getIDbyToken(String token){
        Claims claims=Jwts.parser()
                .setSigningKey(base64EncodedSecretKey)
                .parseClaimsJws(token)
                .getBody();
        return  claims.getSubject();
    }

    public static String getRolebyToken(String token){
        Claims claims=Jwts.parser()
                .setSigningKey(base64EncodedSecretKey)
                .parseClaimsJws(token)
                .getBody();
        return  claims.get("roles").toString();
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
