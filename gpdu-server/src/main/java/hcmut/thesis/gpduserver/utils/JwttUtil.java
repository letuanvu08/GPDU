package hcmut.thesis.gpduserver.utils;

import hcmut.thesis.gpduserver.models.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwttUtil {

  @Value("${SECRET_KEY}")
  private String SECRET_KEY;

  private final static long durationToken = 1000L * 60 * 60 * 24;
  private final static long durationRefreshToken = 1000L * 60 * 60 * 24 * 30;

  public String extractSubject(String token) {
    return extractClaims(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaims(token, Claims::getExpiration);
  }

  public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public String generateToken(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", user.getId().toString());
    claims.put("typUser", user.getTypeUser());
    claims.put("userName", user.getUserName());
    return createToken(claims, user.getUserName().toString(), durationToken);
  }

  public String generateRefreshToken(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", user.getId().toString());
    claims.put("typUser", user.getTypeUser());
    claims.put("userName", user.getUserName());
    return createToken(claims, user.getId().toString(), durationRefreshToken);
  }

  private String createToken(Map<String, Object> claims, String subject, Long duration) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date((System.currentTimeMillis())))
        .setExpiration(new Date(System.currentTimeMillis() +duration))
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
        .compact();
  }

  public boolean validateToken(String token, User user) {
    final String id = extractSubject(token);
    return (id.equals(user.getId().toString())) && !isTokenExpired(token);
  }

}
