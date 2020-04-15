package com.reactiveworks.restfilters.resource;

import java.util.List;

import javax.annotation.security.DenyAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.reactiveworks.restfilters.dao.EmployeeDao;
import com.reactiveworks.restfilters.model.Employee;



@Path("employees")
public class EmployeeResource {
	EmployeeDao employeeDao = new EmployeeDao();

	@DenyAll
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getEmployees() {
		List<Employee> employees  = employeeDao.getEmployees();
		return Response.status(200).entity(employees).build();
	}

	@DenyAll
	@GET
	@Path("employee/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getEmployee(@PathParam("id") int id) {
		Employee employee = null;
		employee = employeeDao.getEmployee(id);
		if (employee.getId() == 0) {
			return Response.status(204).entity("employee not found").build();
		}
		return Response.status(200).entity(employee).build();

	}

}
