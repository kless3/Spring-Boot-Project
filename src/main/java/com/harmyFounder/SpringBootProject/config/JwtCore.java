package com.harmyFounder.SpringBootProject.config;

import com.harmyFounder.SpringBootProject.service.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtCore {

    @Value("${project.app.secret}")
    private String secret;

    @Value("${project.app.lifetime}")
    private int lifetime;

    public String generateToken(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return buildToken(userDetails.getUsername());
        } else if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            String email = oAuth2User.getAttribute("email");
            return buildToken(email);
        }
        throw new IllegalArgumentException("Unsupported authentication type");
    }

    private String buildToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date().getTime() + lifetime)))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getNameFromJwt(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }


    //added just now
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getNameFromJwt(token);
        return (username.equals(userDetails.getUsername()));
    }
}
