package kz.sayat.webflux_auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends ApiException{


    public UnauthorizedException( String errorMessage) {
        super("Unauthorized", errorMessage);
    }
}
