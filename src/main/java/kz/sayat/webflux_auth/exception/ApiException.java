package kz.sayat.webflux_auth.exception;

import lombok.Getter;

public class ApiException extends RuntimeException {

    @Getter
    protected String errorCode;



    public ApiException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
