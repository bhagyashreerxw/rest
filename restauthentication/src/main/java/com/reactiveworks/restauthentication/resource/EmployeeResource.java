package com.reactiveworks.restauthentication.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.reactiveworks.restauthentication.dao.EmployeeDao;
import com.reactiveworks.restauthentication.model.Employee;


@Path("employees")
public class EmployeeResource {
	EmployeeDao employeeDao = new EmployeeDao();

	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getEmployees() {
		List<Employee> employees = employeeDao.getEmployees();
		return Response.status(200).entity(employees).build();
	}

	
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