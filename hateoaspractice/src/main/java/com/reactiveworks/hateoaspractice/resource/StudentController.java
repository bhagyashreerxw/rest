package com.reactiveworks.hateoaspractice.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.reactiveworks.hateoaspractice.Dao.StudentDao;
import com.reactiveworks.hateoaspractice.db.eceptions.DBOperationFailureException;
import com.reactiveworks.hateoaspractice.db.eceptions.DataBaseAccessException;
import com.reactiveworks.hateoaspractice.db.eceptions.InvalidDBRecordFormatException;
import com.reactiveworks.hateoaspractice.db.eceptions.StudentNotFoundException;
import com.reactiveworks.hateoaspractice.model.Student;

@Path("students")
public class StudentController {
	StudentDao studentDao = new StudentDao();

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getStudents(@Context UriInfo uriInfo) throws StudentNotFoundException {
		List<Student> students = null;

		try {
			students = studentDao.getStudents();
			if (students == null) {
				throw new StudentNotFoundException("database doesn't have student records");
			}

		} catch (DataBaseAccessException | InvalidDBRecordFormatException | DBOperationFailureException e) {

			e.printStackTrace();
		}
		for (Student student : students) {
			student.addLink(uriToSelf(uriInfo, student), "self");
			student.addLink(uriToDelete(uriInfo, student), "deleteStudent");
			student.addLink(uriToUpdateStudent(uriInfo, student), "updateStudent");
		}

		// return students;
		return Response.status(Status.OK).entity(students).build();
	}

	@GET
	@Path("/{rollNo}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getStudent(@PathParam("rollNo") int rollNo, @Context UriInfo uriInfo)
			throws StudentNotFoundException {
		Student student = null;

		try {
			student = studentDao.getStudent(rollNo);
			if (student.getRollNo() == 0) {

				throw new StudentNotFoundException("student with the given rollNo is not found");
			}
		} catch (DataBaseAccessException | DBOperationFailureException | InvalidDBRecordFormatException e) {

			e.printStackTrace();
		}
		student.addLink(uriToSelf(uriInfo, student), "self");
		student.addLink(uriToDelete(uriInfo, student), "deleteStudent");
		student.addLink(uriToUpdateStudent(uriInfo, student), "updateStudent");
		return Response.status(Status.OK).entity(student).build();
	}

	private String uriToUpdateStudent(UriInfo uriInfo, Student student) {

		return uriInfo.getBaseUriBuilder().path(StudentController.class).path(StudentController.class, "updateStudent")
				.resolveTemplate("rollNo", student.getRollNo()).resolveTemplate("percentage", student.getPercentage())
				.build().toString();
	}

	@POST
	@Path("/{rollNo}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createStudent(@PathParam("rollNo") int rollNo, Student student, @Context UriInfo uriInfo) {
		try {

			studentDao.createStudent(student);
		} catch (DataBaseAccessException | InvalidDBRecordFormatException e) {

			e.printStackTrace();
		}
		student.addLink(uriToSelf(uriInfo, student), "self");
		student.addLink(uriToDelete(uriInfo, student), "deleteStudent");
		student.addLink(uriToUpdateStudent(uriInfo, student), "updateStudent");
		return Response.status(Status.CREATED).entity(student).build();
	}

	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createStudents(List<Student> students, @Context UriInfo uriInfo) {
		try {
			studentDao.createStudents(students);
		} catch (DataBaseAccessException | InvalidDBRecordFormatException e) {

			e.printStackTrace();
		}
		for (Student student : students) {
			student.addLink(uriToSelf(uriInfo, student), "self");
			student.addLink(uriToDelete(uriInfo, student), "deleteStudent");
			student.addLink(uriToUpdateStudent(uriInfo, student), "updateStudent");
		}
		return Response.status(Status.CREATED).entity(students).build();
	}

	@PUT
	@Path("/{rollNo}/{percentage}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateStudent(@PathParam("rollNo") int rollNo, @PathParam("percentage") double percentage,
			@Context UriInfo uriInfo) throws StudentNotFoundException {
		Student student = null;
		try {
			if (studentDao.getStudent(rollNo).getRollNo() == 0) {

				throw new StudentNotFoundException("student with given rollNo is not found");
			} else {

				studentDao.updateStudent(rollNo, percentage);
				student = studentDao.getStudent(rollNo);

			}
		} catch (DataBaseAccessException | DBOperationFailureException | InvalidDBRecordFormatException e) {

			e.printStackTrace();
		}
		student.addLink(uriToSelf(uriInfo, student), "self");
		student.addLink(uriToDelete(uriInfo, student), "deleteStudent");
		student.addLink(uriToCreateStudent(uriInfo, student), "createStudent");
		return Response.status(Status.OK).entity(student).build();
	}

	@DELETE
	@Path("/{rollNo}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteStudent(@PathParam("rollNo") int rollNo) {
		try {
			if (studentDao.getStudent(rollNo).getRollNo() != 0) {
				studentDao.deleteStudent(rollNo);
				return Response.status(Status.OK).build();
			} else {
				studentDao.deleteStudents();
			}

		} catch (DataBaseAccessException | DBOperationFailureException | InvalidDBRecordFormatException e) {

			e.printStackTrace();
		}
		return Response.status(Status.OK).build();

	}

	private String uriToDelete(UriInfo uriInfo, Student student) {

		return uriInfo.getBaseUriBuilder().path(StudentController.class).path(StudentController.class, "deleteStudent")
				.resolveTemplate("rollNo", student.getRollNo())

				.build().toString();
	}

	private String uriToCreateStudent(UriInfo uriInfo, Student student) {
		return uriInfo.getBaseUriBuilder().path(StudentController.class)

				.build().toString() + "/" + Long.toString(student.getRollNo());
	}

	private String uriToSelf(UriInfo uriInfo, Student student) {
		return uriInfo.getBaseUriBuilder().path(StudentController.class).path(Long.toString(student.getRollNo()))
				.build().toString();
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