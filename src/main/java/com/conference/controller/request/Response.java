package com.conference.controller.request;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public record Response<T>( String message, String errorCode) {

}
