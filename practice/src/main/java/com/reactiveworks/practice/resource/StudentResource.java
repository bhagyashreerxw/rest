package com.reactiveworks.practice.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.reactiveworks.practice.Dao.StudentDao;
import com.reactiveworks.practice.db.eceptions.DBOperationFailureException;
import com.reactiveworks.practice.db.eceptions.DataBaseAccessException;
import com.reactiveworks.practice.db.eceptions.InvalidDBRecordFormatException;
import com.reactiveworks.practice.model.Student;

@Path("students")
public class StudentResource {
	StudentDao studentDao = new StudentDao();

	@GET
	@Path("student")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getStudents() {
		List<Student> students = null;

		try {
			students = studentDao.getStudents();

		} catch (DataBaseAccessException | InvalidDBRecordFormatException | DBOperationFailureException e) {

			e.printStackTrace();
		}

		// return students;
		return Response.status(Status.OK).entity(students).build();
	}

	@GET
	@Path("student/{rollNo}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getStudent(@PathParam("rollNo") int rollNo){
		Student student = null;
		
		try {
			student = studentDao.getStudent(rollNo);
			if (student.getRollNo() == 0) {
                
				return Response.status(404).entity("student not  found").build();
			}
		} catch (DataBaseAccessException | DBOperationFailureException | InvalidDBRecordFormatException e) {

			e.printStackTrace();
		}

		return Response.status(Status.OK).entity(student).build();
	}

	@POST
	@Path("student/{id}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createStudent(Student student) {
		try {
			studentDao.createStudent(student);
		} catch (DataBaseAccessException | InvalidDBRecordFormatException e) {

			e.printStackTrace();
		}

		return Response.status(Status.CREATED).build();
	}

	@POST
	@Path("student")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void createStudents(List<Student> students) {
		try {
			studentDao.createStudents(students);
		} catch (DataBaseAccessException | InvalidDBRecordFormatException e) {

			e.printStackTrace();
		}
	}

	@PUT
	@Path("student/{id}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateStudent(Student student) {

		try {
			if (studentDao.getStudent(student.getRollNo()).getRollNo() == 0) {
				studentDao.createStudent(student);
			} else {
				//studentDao.updateStudent(student);
				System.out.println("updateStudent");

			}
		} catch (DataBaseAccessException | DBOperationFailureException | InvalidDBRecordFormatException e) {

			e.printStackTrace();
		}
		return Response.status(Status.OK).build();
	}

	@DELETE
	@Path("student/{rollNo}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteStudent(@PathParam("rollNo") int rollNo) {
		try {
			if (studentDao.getStudent(rollNo).getRollNo() != 0) {
				studentDao.deleteStudent(rollNo);
				return Response.status(Status.OK).build();
			}

		} catch (DataBaseAccessException | DBOperationFailureException | InvalidDBRecordFormatException e) {

			e.printStackTrace();
		}
		return Response.status(Status.NO_CONTENT).build();
	}

//	@DELETE
//	//@Path("student/{rollNo}")
//	@Path("student")
//	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	public void deleteStudent(@QueryParam("rollNo") int rollNo) {
//		try {
//			if (studentDao.getStudent(rollNo).getRollNo() != 0) {
//				studentDao.deleteStudent(rollNo);
//			}
//
//		} catch (DataBaseAccessException | DBOperationFailureException | InvalidDBRecordFormatException e) {
//
//			e.printStackTrace();
//		}
//	}

}