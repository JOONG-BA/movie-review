package org.dfpl.lecture.db.backend.util;

import io.jsonwebtoken.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JwtUtil {

    private final String SECRET = "my-secret-key";
    private final long EXPIRATION = 1000 * 60 * 60*24;

    public String generateToken(String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public String generateToken(String email) {
        return generateToken(email, List.of("USER"));
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        Object claim = parseClaims(token).get("roles");
        if (claim instanceof List<?>) {
            // (List<?>) 를 String 리스트로 변환
            return ((List<?>) claim).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }
        return List.of();  // null 대신 빈 리스트 리턴
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
