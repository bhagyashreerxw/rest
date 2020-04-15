package com.reactiveworks.practice.response;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.reactiveworks.practice.model.Student;
import com.reactiveworks.practice.repo.StudentRepository;

@Path("students")
public class StudentResource {
	
	StudentRepository repo=new StudentRepository();
	
	
	@GET
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public List<Student> getStudent() {
		System.out.println("getStudent() is called");
		return repo.getStudents();
	}
	
	@GET
	@Path("student/{rollNo}")
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Student getStudent(@PathParam("rollNo") int rollNo) {
		Student student=repo.getStudent(rollNo);
		return student;
	}
	
	@POST
	@Path("student")
	public Response createStudent(Student student) {
		
		System.out.println(student);
		Student studentObj=repo.createStudent(student);
		return Response.status(Status.ACCEPTED)
				.entity(studentObj).build();
	}

}
