package com.reactiveworks.exceptionmapping.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class StudentNotFoundException extends Exception implements ExceptionMapper<StudentNotFoundException> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StudentNotFoundException(String message) {
		super(message);
	}
	
	public StudentNotFoundException() {
		
	}
	
	@Override
	public Response toResponse(StudentNotFoundException exception) {
	     ErrorResponse error=new ErrorResponse();
	     error.setMessage(exception.getMessage());
	     error.setStatusCode(404);
		 return Response.status(Response.Status.NOT_FOUND).entity(error).build();
	}
	
	

}
