package com.reactiveworks.pagination.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.reactiveworks.pagination.dao.StudentDao;
import com.reactiveworks.pagination.db.exceptions.DBOperationFailureException;
import com.reactiveworks.pagination.db.exceptions.DataBaseAccessException;
import com.reactiveworks.pagination.db.exceptions.InvalidDBRecordFormatException;
import com.reactiveworks.pagination.db.exceptions.StudentNotFoundException;
import com.reactiveworks.pagination.model.PageContent;
import com.reactiveworks.pagination.model.PageLink;
import com.reactiveworks.pagination.model.Student;

@Path("students")
public class StudentController {
	StudentDao studentDao = new StudentDao();

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getStudents(@Context UriInfo uriInfo, @QueryParam("start") int start, @QueryParam("size") int size)
			throws StudentNotFoundException, DataBaseAccessException, InvalidDBRecordFormatException,
			DBOperationFailureException {
		List<Student> students = studentDao.getStudents();
		if (students == null) {
			throw new StudentNotFoundException("database doesn't have student records");
		}

		for (Student student : students) {
			student.addLink(uriToSelf(uriInfo, student), "self");
		}

		if (start + size <= students.size()) {

			if (start >= 0 && size > 0) {
				// List<List<Student>> subLists=new ArrayList<List<Student>>();
				List<PageContent> subList = new ArrayList<PageContent>();

				for (int i = start; i + size < students.size(); i += size) {
					PageLink link = new PageLink();
					// subLists.add(students.subList(i, i + size));

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
				// subLists.add(students.subList(i, students.size()));

				// return Response.status(Status.OK).entity(subLists.get(0)).build();
				return Response.status(Status.OK).entity(subList.get(0)).build();
			}

		}

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
		return Response.status(Status.OK).entity(student).build();
	}

	private String uriToSelf(UriInfo uriInfo, Student student) {
		return uriInfo.getBaseUriBuilder().path(StudentController.class).path(Long.toString(student.getRollNo()))
				.build().toString();
	}

}