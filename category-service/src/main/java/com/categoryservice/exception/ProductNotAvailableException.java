package com.categoryservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class ProductNotAvailableException extends RuntimeException{
	private static final long serialVersionUID = 1348771109171435607L;
    public ProductNotAvailableException(String message){
        super(message);
    }

    public ProductNotAvailableException(String message, Throwable cause){
        super(message, cause);
    }
}