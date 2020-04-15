package com.reactiveworks.iplrestservice.resources.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.reactiveworks.iplrestservice.resources.exceptions.response.ErrorResponse;

@Provider
public class DeliveryResourceNotFoundException extends Exception implements ExceptionMapper<DeliveryResourceNotFoundException>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeliveryResourceNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DeliveryResourceNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public DeliveryResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public DeliveryResourceNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public DeliveryResourceNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response toResponse(DeliveryResourceNotFoundException exception) {
		ErrorResponse error=new ErrorResponse();
		
		error.setMessage(exception.getMessage());
		error.setStatusCode(204);
		System.out.println(error);
		return Response.status(204).entity(error).build();
	}

}
