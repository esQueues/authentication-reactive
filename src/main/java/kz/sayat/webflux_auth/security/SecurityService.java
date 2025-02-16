package kz.sayat.webflux_auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kz.sayat.webflux_auth.entity.UserEntity;
import kz.sayat.webflux_auth.exception.AuthException;
import kz.sayat.webflux_auth.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Integer expirationInSeconds;
    @Value("${jwt.issuer}")
    private String issuer;

    private TokenDetails generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>(){
            {
                put("role", user.getRole());
                put("username", user.getUsername());
            }
        };
        return generateToken(claims,user.getId().toString());
    };


    private TokenDetails generateToken(Map<String, Object> claims,String subject) {
        Long expirationTimeInMillis = expirationInSeconds * 1000L;
        Date expirationDate= new Date(new Date().getTime() + expirationTimeInMillis);

        return generateToken(expirationDate,claims,subject);
    }

    private TokenDetails generateToken(Date expirationDate, Map<String, Object> claims, String subject)
    {
        Date createdDate = new Date();
        String token= Jwts.builder()
            .setClaims(claims)
            .setIssuer(issuer)
            .setSubject(subject)
            .setIssuedAt(createdDate)
            .setId(UUID.randomUUID().toString())
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes())       )
            .compact();

        return TokenDetails.builder()
            .token(token)
            .issuedAt(createdDate)
            .expiresAt(expirationDate)
            .build();
    }


    public Mono<TokenDetails> authenticate(String username,String password ) {
        return userRepository.findByUsername(username)
            .flatMap(user ->{
                if(!user.isEnabled()){
                    return Mono.error(new AuthException("PROSELYTE_USER_ACCOUNT_DISABLED","Account disables"));
                }
                if(!passwordEncoder.matches(password,user.getPassword())){
                    return Mono.error(new AuthException("PROSELYTE_USER_PASSWORD_INCORRECT","Password incorrect"));
                }

                return Mono.just(generateToken(user).toBuilder()
                    .userId(user.getId())
                    .build());
            })
            .switchIfEmpty(Mono.error(new AuthException("PROSELYTE_USER_PASSWORD_INCORRECT","Username incorrect")));
    }


}
