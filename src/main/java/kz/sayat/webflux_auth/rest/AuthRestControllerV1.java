package kz.sayat.webflux_auth.rest;

import kz.sayat.webflux_auth.dto.AuthRequestDto;
import kz.sayat.webflux_auth.dto.AuthResponseDto;
import kz.sayat.webflux_auth.dto.UserDto;
import kz.sayat.webflux_auth.entity.UserEntity;
import kz.sayat.webflux_auth.mapper.UserMapper;
import kz.sayat.webflux_auth.repository.UserRepository;
import kz.sayat.webflux_auth.security.CustomPrinciple;
import kz.sayat.webflux_auth.security.SecurityService;
import kz.sayat.webflux_auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthRestControllerV1 {
    private final SecurityService securityService;
    private final UserService userService;
    private final UserMapper userMapper;


    @PostMapping("/register")
    public Mono<UserDto> register(@RequestBody UserDto dto) {
        UserEntity entity = userMapper.map(dto);
        return userService.registerUser(entity)
            .map(userMapper::map);
    }

    @PostMapping("/login")
    public Mono<AuthResponseDto> login(@RequestBody AuthRequestDto dto) {
        return securityService.authenticate(dto.getUsername(), dto.getPassword())
            .flatMap(tokenDetails -> Mono.just(
                AuthResponseDto.builder()
                    .userId(tokenDetails.getUserId())
                    .token(tokenDetails.getToken())
                    .issuedAt(tokenDetails.getIssuedAt())
                    .expiresAt(tokenDetails.getExpiresAt())
                    .build()
            ));
    }

    @GetMapping("/info")
    public Mono<UserDto> getUserInfo(Authentication authentication) {
        CustomPrinciple customPrincipal = (CustomPrinciple) authentication.getPrincipal();


        return userService.getUserById(customPrincipal.getId())
            .map(userMapper::map);
    }


}
