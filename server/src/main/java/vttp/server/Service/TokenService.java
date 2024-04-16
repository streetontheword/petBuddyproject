package vttp.server.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.Jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;


import vttp.server.Configuration.SecurityConfig;
import vttp.server.Model.UserPrincipal;

@Service
public class TokenService {

    @Autowired 
    SecurityConfig secConfig;

    @Autowired 
    JwtDecoder jwtDecode;

    @Value("${jwt.key.secret}")
    private String secretKey;
    public String generateToken(Authentication auth) {
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Instant now = Instant.now();
        String scope = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return JWT.create()
                .withIssuer("PetBuddy")
                .withIssuedAt(now)
                .withSubject(String.valueOf(userPrincipal.getUser().getUserId()))
                .withExpiresAt(now.plus(1, ChronoUnit.HOURS)) //this determines the timing of the token 
                .withClaim("name", auth.getName())
                .withClaim("roles",String.valueOf(userPrincipal.getUser().getRole()))
                .withClaim("scope", scope)
                .sign(Algorithm.HMAC256(secretKey));
    }



    public String getUserIdFromToken(String token){
        Jwt jwt = jwtDecode.decode(token);
        //  System.out.println("roles>>>" + jwt.getClaimAsString("roles"));
        // System.out.println("subject>>>>>"+jwt.getSubject());
        return jwt.getSubject();
    }

    public String getRoleFromToken(String token){
        Jwt jwt = jwtDecode.decode(token);
        return jwt.getClaimAsString("roles");
    }
    
    
     
}
