package task.company.local.config;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import task.company.local.implement.UserService;

@Component
public class JwtService {

    @Value("${app.jwt-SECRET}")
    private String SECRET;

    private Key key;

    @Autowired
    private UserService UserService;

    @PostConstruct
    public void init() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET);
        if (decodedKey.length < 32) {
            throw new IllegalArgumentException("Kunci harus setidaknya 32 byte panjangnya");
        }
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.get("id", Long.class);
    }

    public Authentication getAuthentication(String token) {
        String email = decodeToken(token);
        if (email != null) {
            UserDetails userDetails = UserService.loadUserByUsername(email); // Load user by email
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        }
        return null;
    }

    public String decodeToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject(); // Mengambil email dari subject token
        } catch (Exception e) {
            String Error = "kesalahan pada decode Token";
            return Error;
        }
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String generateToken(String email, Long Id) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email, Id);
    }

    private String createToken(Map<String, Object> claims, String email, Long Id) {
        claims.put("id", Id);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
