package com.reactiveworks.restexample.db.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.reactiveworks.restexample.errorresponse.ErrorResponse;



public class StudentNotFoundException extends Exception implements ExceptionMapper<StudentNotFoundException>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StudentNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StudentNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public StudentNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public StudentNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public StudentNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Response toResponse(StudentNotFoundException exception) {
	     ErrorResponse error=new ErrorResponse();
	     error.setMessage(exception.getMessage());
	     error.setStatusCode(404);
		 return Response.status(Response.Status.NOT_FOUND).entity(error).build();
	}

}
