package config;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map.Entry;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.RsaSigner;
import io.jsonwebtoken.impl.crypto.RsaSignatureValidator;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public final class MyTokenHandler {
	 
    PrivateKey privateKey;
    PublicKey publicKey;
 
    public MyTokenHandler(PrivateKey privateKey, PublicKey publicKey) {
    	this.privateKey = privateKey;
    	this.publicKey = publicKey;
    }
 
    public User parseUserFromToken(String token) {
        try{
        	Claims claims = Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(token)
                .getBody();
            String username = claims.getSubject();
            ArrayList<String> roles = (ArrayList<String>) claims.get("groups");
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority((String) roles.get(0));
            HashSet<SimpleGrantedAuthority> authorities = new HashSet<SimpleGrantedAuthority>();
            authorities.add(authority);
            User user = new User(username, "", authorities);
            return user;
        } catch(Exception e) {
        	System.out.println("Key not valid");
        }
        return null;
    }
 
    public String createTokenForUser(User user) {
    	HashSet<String> roles = new HashSet<String>();
    	roles.add(user.getAuthorities().toArray()[0].toString());
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("groups", roles)
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }
    
    public String toTable(String token) {
        
    	String ret = "";
    	Jws jws;
    	
    	try{
        	jws = Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token);

        } catch(Exception e) {
        	System.out.println("Key not valid");
            return "";
        }
    	
    	ret = ret + "<table><tr><td>Header:</td><td></td><td></td></tr>";

    	Header header = jws.getHeader();
		Object[] arr = header.entrySet().toArray();
		for(int i = 0; i < arr.length; i++) {
			String line = arr[i].toString();
			line = "<tr><td></td><td>" + line.split("=")[0] + ":</td><td>" + line.split("=")[1] + "</td></tr>";
			ret = ret + line;
		}
		
		ret = ret + "<table><tr><td>Payload:</td><td></td><td></td></tr>";
		
    	Claims claims = (Claims) jws.getBody();
		arr = claims.entrySet().toArray();
		for(int i = 0; i < arr.length; i++) {
			String line = arr[i].toString();
			line = "<tr><td></td><td>" + line.split("=")[0] + ":</td><td>" + line.split("=")[1] + "</td></tr>";
			ret = ret + line;
		}

    	ret = ret + "</table>";
    	    	
    	return ret;
    }
   
}