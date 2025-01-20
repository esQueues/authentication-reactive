package kz.sayat.webflux_auth.exception;

public class AuthException extends ApiException{

    public AuthException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
