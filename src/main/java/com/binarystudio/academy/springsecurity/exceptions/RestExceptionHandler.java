package com.binarystudio.academy.springsecurity.exceptions;

import com.binarystudio.academy.springsecurity.security.jwt.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends AbstractExceptionHandler {

	@ExceptionHandler(ResponseStatusException.class)
	public ApiError handleResponseStatusException(ResponseStatusException exception, HttpServletRequest request, HttpServletResponse response) {
		return setResponseStatusAndReturnError(exception, exception.getReason(), exception.getStatus(), request, response);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ApiError handleEntityNotFound(NoSuchElementException exception, HttpServletRequest request, HttpServletResponse response) {
		return setResponseStatusAndReturnError(exception, "entity-not-found", HttpStatus.NOT_FOUND, request, response);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ApiError handleUsernameNotFound(UsernameNotFoundException exception, HttpServletRequest request, HttpServletResponse response) {
		return setResponseStatusAndReturnError(exception, "username-not-found", HttpStatus.NOT_FOUND, request, response);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ApiError handleUserNotFound(UsernameNotFoundException exception, HttpServletRequest request, HttpServletResponse response) {
		return setResponseStatusAndReturnError(exception, "user-not-found", HttpStatus.NOT_FOUND, request, response);
	}

	@ExceptionHandler(EmailNotFoundException.class)
	public ApiError handleEmailNotFound(UsernameNotFoundException exception, HttpServletRequest request, HttpServletResponse response) {
		return setResponseStatusAndReturnError(exception, "email-not-found", HttpStatus.NOT_FOUND, request, response);
	}

	@ExceptionHandler(HotelNotFoundException.class)
	public ApiError handleHotelNotFound(UsernameNotFoundException exception, HttpServletRequest request, HttpServletResponse response) {
		return setResponseStatusAndReturnError(exception, "hotel-not-found", HttpStatus.NOT_FOUND, request, response);
	}

	@ExceptionHandler(JwtException.class)
	public ApiError handleJwtException(JwtException exception, HttpServletRequest request, HttpServletResponse response) {
		return setResponseStatusAndReturnError(exception, exception.getCode(), HttpStatus.UNAUTHORIZED, request, response);
	}

	@ExceptionHandler(Exception.class)
	public ApiError handleAll(Exception exception, HttpServletRequest request, HttpServletResponse response) {
		log.error("Unhandled error", exception);
		return setResponseStatusAndReturnError(exception, "internal-error", HttpStatus.INTERNAL_SERVER_ERROR, request, response);
	}
}
