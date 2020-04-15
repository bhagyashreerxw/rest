package com.reactiveworks.restexample;

import com.reactiveworks.restexample.dao.StudentDao;
import com.reactiveworks.restexample.db.exceptions.DBOperationFailureException;
import com.reactiveworks.restexample.db.exceptions.DataBaseAccessException;

public class Test {

	public static void main(String[] args) throws DataBaseAccessException, DBOperationFailureException {
		StudentDao dao=new StudentDao();
		System.out.println(dao.getStudents());

	}

}
