package com.example.xiaohui.util;

import io.jsonwebtoken.*;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "sdjadhsahdsahdjksjsabkjsabjksandkjasjskahjskahjasnjkascnjascksahj"; // 替换为你的密钥
    private static final long EXPIRATION_TIME = 86400000L; // 1 天（单位：毫秒）

    // 生成 token
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // 验证 token
    public static boolean verifyToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // 验证 token 是否有效
    public static boolean isValidToken(String token) {
        try {
            // 解析 token 并验证签名
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claims != null;
        } catch (Exception e) {
            // 如果 token 无效（解析错误等），抛出异常
            return false;
        }
    }


}
