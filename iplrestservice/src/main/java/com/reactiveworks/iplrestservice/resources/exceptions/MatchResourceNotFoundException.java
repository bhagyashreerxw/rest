package com.reactiveworks.iplrestservice.resources.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.reactiveworks.iplrestservice.resources.exceptions.response.ErrorResponse;

@Provider
public class MatchResourceNotFoundException extends Exception
		implements ExceptionMapper<MatchResourceNotFoundException> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MatchResourceNotFoundException(String message) {
		super(message);
	}

	public MatchResourceNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MatchResourceNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public MatchResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public MatchResourceNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response toResponse(MatchResourceNotFoundException exception) {
		ErrorResponse error = new ErrorResponse();

		error.setMessage(exception.getMessage());
		error.setStatusCode(404);
		return Response.status(404).entity(error).build();
	}

}
