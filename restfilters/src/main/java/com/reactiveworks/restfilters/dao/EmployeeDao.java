package com.reactiveworks.restfilters.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.reactiveworks.restfilters.dao.util.DBUtil;
import com.reactiveworks.restfilters.model.Employee;


public class EmployeeDao {
	
	public List<Employee> getEmployees(){
		List<Employee> employees = null;
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		try {
			employees=new ArrayList<Employee>();
			connection=DBUtil.getdbconnection();
			statement=connection.prepareStatement("SELECT * FROM employee;");
			resultSet=statement.executeQuery();
			while(resultSet.next()) {
				Employee empObj=new Employee();
				empObj.setId(resultSet.getInt(1));
				empObj.setName(resultSet.getString(2));
				empObj.setSalary(resultSet.getDouble(3));
				employees.add(empObj);
			}
		}catch(SQLException exp) {
			System.out.println("unable to access the database");
		}finally {
			DBUtil.cleanupdbresources(resultSet, statement, connection);
		}
		
		return employees;
	}
	
	public Employee getEmployee(int id){
		Employee empObj = null;
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		try {
			empObj=new Employee();
			connection=DBUtil.getdbconnection();
			statement=connection.prepareStatement("SELECT * FROM employee where id=?;");
			statement.setInt(1, id);
			resultSet=statement.executeQuery();
			if(resultSet.next()) {
				
				empObj.setId(resultSet.getInt(1));
				empObj.setName(resultSet.getString(2));
				empObj.setSalary(resultSet.getDouble(3));
				
			}
		}catch(SQLException exp) {
			System.out.println("unable to access the database");
		} finally {
			DBUtil.cleanupdbresources(resultSet, statement, connection);
		}
		
		return empObj;
	}	
	

}
