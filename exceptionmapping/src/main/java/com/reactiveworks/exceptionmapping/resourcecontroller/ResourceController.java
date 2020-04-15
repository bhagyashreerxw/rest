package com.reactiveworks.exceptionmapping.resourcecontroller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.reactiveworks.exceptionmapping.exception.StudentNotFoundException;
import com.reactiveworks.exceptionmapping.model.Student;

@Path("students")
public class ResourceController {

	@Path("student")
	@GET
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.TEXT_PLAIN})
	public void getStudents() throws StudentNotFoundException {
		Student student=new Student();
		student.setName("name");
		student.setPercentage(89.7);
		student.setRollNo(1);
		throw new StudentNotFoundException("student not found");
	}
	
}
