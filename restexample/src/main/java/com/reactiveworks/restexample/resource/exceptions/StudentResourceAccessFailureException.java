package com.reactiveworks.restexample.resource.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.reactiveworks.restexample.errorresponse.ErrorResponse;

public class StudentResourceAccessFailureException extends Exception implements ExceptionMapper<StudentResourceAccessFailureException>{

	public StudentResourceAccessFailureException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StudentResourceAccessFailureException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public StudentResourceAccessFailureException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public StudentResourceAccessFailureException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public StudentResourceAccessFailureException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Response toResponse(StudentResourceAccessFailureException exception) {
		ErrorResponse error=new ErrorResponse();
		error.setStatusCode(500);
		error.setMessage(exception.getMessage());
		
		return Response.status(500).entity(error).build();
	}
	

}
