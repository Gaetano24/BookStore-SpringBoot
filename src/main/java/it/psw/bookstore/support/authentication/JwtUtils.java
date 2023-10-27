package it.psw.bookstore.support.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtUtils {

    public static String getEmailFromAuthentication(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaim("preferred_username");
        }
        return "";
    }

}
