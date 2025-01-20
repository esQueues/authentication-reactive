package kz.sayat.webflux_auth.security;

import io.jsonwebtoken.Claims;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.List;

public class UserAuthenticationBearer {

    public static Mono<Authentication> create(JwtHandler.VerificationResult verificationResult) {
        Claims claims= verificationResult.claims;
        String subject = claims.getSubject();

        String role= claims.get("role").toString();
        String username= claims.get("username").toString();

        List<SimpleGrantedAuthority> authorities= List.of(new SimpleGrantedAuthority(role));

        Long principalId = Long.parseLong(subject);
        CustomPrinciple principle = new CustomPrinciple(principalId, username);

        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(principle,null,authorities));

    }
}
