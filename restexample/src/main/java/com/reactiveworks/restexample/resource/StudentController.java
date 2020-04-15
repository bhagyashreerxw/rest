package com.reactiveworks.restexample.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.reactiveworks.restexample.dao.StudentDao;
import com.reactiveworks.restexample.db.exceptions.DBOperationFailureException;
import com.reactiveworks.restexample.db.exceptions.DataBaseAccessException;
import com.reactiveworks.restexample.db.exceptions.StudentNotFoundException;
import com.reactiveworks.restexample.model.PageContent;
import com.reactiveworks.restexample.model.PageLink;
import com.reactiveworks.restexample.model.Student;
import com.reactiveworks.restexample.resource.exceptions.StudentResourceAccessFailureException;

@Path("students")
public class StudentController {
	StudentDao studentDao = new StudentDao();

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getStudents(@Context UriInfo uriInfo, @QueryParam("start") int start, @QueryParam("size") int size)
			throws StudentNotFoundException, StudentResourceAccessFailureException {
		List<Student> students = null;

		try {
			students = studentDao.getStudents();
			if (students == null) {
				throw new StudentNotFoundException("database doesn't have student records");
			}

		} catch (DataBaseAccessException | DBOperationFailureException e) {

			throw new StudentResourceAccessFailureException("unable to access the student resource", e);
		}
		for (Student student : students) {
			student.addLink(uriToSelf(uriInfo, student), "self");
			student.addLink(uriToDelete(uriInfo, student), "deleteStudent");
			student.addLink(uriToUpdateStudent(uriInfo, student), "updateStudent");
		}

		if (start + size <= students.size()) {
			if (start >= 0 && size > 0) {

				List<PageContent> subList = new ArrayList<PageContent>();

				for (int i = start; i + size < students.size(); i += size) {
					PageLink link = new PageLink();
					PageContent content = new PageContent();
					content.setStudents(students.subList(i, i + size));

					String uri = uriInfo.getBaseUriBuilder().path(StudentController.class).build().toString();
					uri = uri + "?start=" + (start + size) + "&size=" + size;

					link.setNext(uri);
					if (start - size >= 0) {
						uri = uriInfo.getBaseUriBuilder().path(StudentController.class).build().toString();
						uri = uri + "?start=" + (start - size) + "&size=" + size;
						link.setPrev(uri);
					}

					content.setLink(link);

					subList.add(content);

				}

				return Response.status(Status.OK).entity(subList.get(0)).build();
			}
		}

		return Response.status(Status.OK).entity(students).build();
	}

	@GET
	@Path("/{rollNo}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getStudent(@PathParam("rollNo") int rollNo, @Context UriInfo uriInfo)
			throws StudentNotFoundException, StudentResourceAccessFailureException {
		Student student = null;

		try {
			student = studentDao.getStudent(rollNo);
			if (student.getRollNo() == 0) {

				throw new StudentNotFoundException("student with the given rollNo is not found");
			}
		} catch (DataBaseAccessException | DBOperationFailureException e) {

			throw new StudentResourceAccessFailureException("unable to access the student resource", e);
		}
		student.addLink(uriToSelf(uriInfo, student), "self");
		student.addLink(uriToDelete(uriInfo, student), "deleteStudent");
		return Response.status(Status.OK).entity(student).build();
	}

	@POST
	@Path("/{rollNo}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createStudent(@PathParam("rollNo") int rollNo, Student student, @Context UriInfo uriInfo)
			throws StudentResourceAccessFailureException {
		try {
			if (studentDao.getStudent(rollNo).getRollNo() == 0) {
				studentDao.createStudent(student);
			} else {
				studentDao.updateStudent(rollNo, student);
			}

		} catch (DataBaseAccessException | DBOperationFailureException e) {

			throw new StudentResourceAccessFailureException("unable to access the student resource", e);
		}
		student.addLink(uriToSelf(uriInfo, student), "self");
		student.addLink(uriToDelete(uriInfo, student), "deleteStudent");
		student.addLink(uriToUpdateStudent(uriInfo, student), "updateStudent");
		return Response.status(Status.CREATED).entity(student).build();
	}

	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createStudents(List<Student> students, @Context UriInfo uriInfo)
			throws StudentResourceAccessFailureException {
		try {
			studentDao.createStudents(students);
		} catch (DataBaseAccessException e) {

			throw new StudentResourceAccessFailureException("unable to access the student resource", e);
		}
		for (Student student : students) {
			student.addLink(uriToSelf(uriInfo, student), "self");
			student.addLink(uriToUpdateStudent(uriInfo, student), "updateStudent");
		}
		return Response.status(Status.CREATED).entity(students).build();
	}

	@PUT
	@Path("/{rollNo}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateStudent(@PathParam("rollNo") int rollNo, Student student, @Context UriInfo uriInfo)
			throws StudentResourceAccessFailureException {

		try {
			if (studentDao.getStudent(rollNo).getRollNo() == 0) {

				studentDao.createStudent(student);
			} else {

				studentDao.updateStudent(rollNo, student);
				student = studentDao.getStudent(rollNo);

			}
		} catch (DataBaseAccessException | DBOperationFailureException e) {

			throw new StudentResourceAccessFailureException("unable to access the student resource", e);
		}
		student.addLink(uriToSelf(uriInfo, student), "self");
		student.addLink(uriToCreateStudent(uriInfo, student), "createStudent");
		return Response.status(Status.OK).entity(student).build();
	}

	@DELETE
	@Path("/{rollNo}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteStudent(@PathParam("rollNo") int rollNo) throws StudentResourceAccessFailureException {
		try {
			if (studentDao.getStudent(rollNo).getRollNo() != 0) {
				studentDao.deleteStudent(rollNo);
				return Response.status(Status.OK).build();
			} else {
				studentDao.deleteStudents();
			}

		} catch (DataBaseAccessException | DBOperationFailureException e) {

			throw new StudentResourceAccessFailureException("unable to access the student resource", e);
		}
		return Response.status(Status.OK).build();

	}

	@PATCH
	@Path("/{rollNo}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateStudent(@PathParam("rollNo") int rollNo, Student student)
			throws StudentResourceAccessFailureException, StudentNotFoundException {
		try {
			if (studentDao.getStudent(rollNo).getRollNo() == 0) {
				throw new StudentNotFoundException("student not found");
			} else {
				studentDao.updateStudent(rollNo, student);
			}

		} catch (DataBaseAccessException | DBOperationFailureException e) {

			throw new StudentResourceAccessFailureException("unable to access the student resource", e);
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

	private String uriToUpdateStudent(UriInfo uriInfo, Student student) {

		return uriInfo.getBaseUriBuilder().path(StudentController.class).path(StudentController.class, "updateStudent")
				.resolveTemplate("rollNo", student.getRollNo()).build().toString();
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