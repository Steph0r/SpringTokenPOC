package config;

import java.util.HashSet;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public final class MyTokenHandler {
	 
    private final String secret;
 
    public MyTokenHandler(String secret) {
        this.secret = secret;
    }
 
    public User parseUserFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();            
        String username = claims.getSubject();
        String role = (String) claims.get("roles");
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
        HashSet<SimpleGrantedAuthority> authorities = new HashSet<SimpleGrantedAuthority>();
        authorities.add(authority);
        User user = new User(username, "", authorities);
        return user;
    }
 
    public String createTokenForUser(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getUsername())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}