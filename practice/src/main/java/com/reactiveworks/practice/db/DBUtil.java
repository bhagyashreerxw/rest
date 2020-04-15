package com.reactiveworks.practice.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;

import com.reactiveworks.practice.db.eceptions.DBOperationFailureException;
import com.reactiveworks.practice.db.eceptions.DataBaseAccessException;

public class DBUtil {

	private static final Logger LOGGER_OBJ = Logger.getLogger("DBUtil.class");
	private static final String PROPERTY_FILE = "database.properties";
	private static final String URL = "url";
	private static final String USER_NAME = "username";
	private static final String PASSWORD = "password";
	private static BasicDataSource dataSource = null;

	private DBUtil() {

	}

	public static synchronized Connection getdbconnection()
			throws DataBaseAccessException, DBOperationFailureException {
	
		Connection connection = null;
		if (dataSource == null) {
			dataSource = new BasicDataSource();
			try (InputStream input = DBUtil.class.getClassLoader().getResourceAsStream(PROPERTY_FILE)) {

				Properties properties = new Properties();
				properties.load(input);
				dataSource.setDriverClassName(properties.getProperty("driver.class.name"));
				dataSource.setUrl(properties.getProperty(URL));
				dataSource.setUsername(properties.getProperty(USER_NAME));
				dataSource.setPassword(properties.getProperty(PASSWORD));

			} catch (IOException exp) {
				LOGGER_OBJ.error("not able to read the properties file " + PROPERTY_FILE);
				throw new DBOperationFailureException("not able to read the file " + PROPERTY_FILE, exp);
			} catch (NumberFormatException exp) {
				LOGGER_OBJ.error("format of dataSource property is invalid .");
				throw new DataBaseAccessException("format of dataSource property is invalid  " + exp);
			}

		}

		try {

			connection = dataSource.getConnection();

		} catch (SQLException exp) {
			LOGGER_OBJ.error(" unable to access the stocktrade database ");
			throw new DataBaseAccessException("unable to access the stocktrade database " , exp);
		}
		
		return connection;
	}

	public static void cleanupdbresources(ResultSet resultSet, PreparedStatement pstatement, Connection connection) {
	
		try {
			if (resultSet != null) {
				resultSet.close();
			}
		} catch (SQLException exp) {
			LOGGER_OBJ.error(" unable to close resultSet resource ");
		} finally {
			try {
				if (pstatement != null) {
					pstatement.close();
				}
			} catch (SQLException exp) {
				LOGGER_OBJ.error(" unable to access the stocktrade database ");
			} finally {
				try {
					if (connection != null) {
						connection.close();
					}
				} catch (SQLException exp) {
					LOGGER_OBJ.error(" unable to access the stocktrade database ");
				}

			}
		}
	}
}