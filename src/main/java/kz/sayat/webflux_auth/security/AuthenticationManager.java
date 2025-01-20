package kz.sayat.webflux_auth.security;

import kz.sayat.webflux_auth.entity.UserEntity;
import kz.sayat.webflux_auth.exception.UnauthorizedException;
import kz.sayat.webflux_auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final UserRepository userRepository;



    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        CustomPrinciple principle= (CustomPrinciple) authentication.getPrincipal();


        return userRepository.findById(principle.getId())
            .filter(UserEntity::isEnabled)
            .switchIfEmpty(Mono.error(new UnauthorizedException("User disabled")))
            .map(user -> authentication);
    }
}
