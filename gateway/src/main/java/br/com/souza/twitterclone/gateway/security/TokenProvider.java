package br.com.souza.twitterclone.gateway.security;

import br.com.souza.twitterclone.gateway.properties.SecurityProperties;
import br.com.souza.twitterclone.gateway.service.RedisService;
import br.com.souza.twitterclone.gateway.util.UsefulDate;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenProvider {

    private static final int UNAUTHORIZED = 401;
    private static final int EXPIRED = 440;
    private final SecurityProperties securityProperties;
    private final RedisService redisService;

    public TokenProvider(SecurityProperties securityProperties,
                         RedisService redisService) {
        this.securityProperties = securityProperties;
        this.redisService = redisService;
    }

    public void isValid(String jwt) throws Exception{
        try {
            jwt = extractToken(jwt);
            String secret = securityProperties.getJwtKey();

            SignedJWT signedJWT = SignedJWT.parse(jwt);
            JWSVerifier verifier = new MACVerifier(securityProperties.getJwtKey().getBytes(StandardCharsets.UTF_8));
            if (!signedJWT.verify(verifier)) {
                log.error("Invalid JWT token!");
                throw new Exception("Token expirado: {}");
            }
            Claims claims = Jwts.parser().setSigningKey(secret.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(jwt).getBody();
            String sessionIdentifier = claims.getSubject();
            String identifier = (String) redisService.getValue(sessionIdentifier);
            if(identifier == null){
                throw new Exception("Token não existe no redis.");
            }
        } catch (ExpiredJwtException e) {
            log.error("Token expirado: ", e);
            throw new Exception("Token expirado: {}", e);
        }catch (Exception e) {
            log.error("Token inválido: ", e);
            throw new Exception("Token inválido: {}");
        }
    }

    public String getIdentifierFromToken(String jwt) {
        jwt = extractToken(jwt);
        String secret = securityProperties.getJwtKey();
        Claims claims = Jwts.parser().setSigningKey(secret.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(jwt).getBody();
        String sessionIdentifier = claims.getSubject();
        return (String) redisService.getValue(sessionIdentifier);
    }

    private String extractToken(String authToken) {
        if (authToken.toLowerCase().startsWith("bearer")) {
            return authToken.substring("bearer ".length());
        }
        return authToken;
    }
}