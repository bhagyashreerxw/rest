package com.reactiveworks.restfilters.dao.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;

public class DBUtil {

	private static final String PROPERTY_FILE = "database.properties";
	private static final String URL = "url";
	private static final String USER_NAME = "username";
	private static final String PASSWORD = "password";
	private static BasicDataSource dataSource = null;

	private DBUtil() {

	}

	public static synchronized Connection getdbconnection() {

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

			} catch (NumberFormatException exp) {

			}

		}

		try {

			connection = dataSource.getConnection();

		} catch (SQLException exp) {

		}

		return connection;
	}

	public static void cleanupdbresources(ResultSet resultSet, PreparedStatement pstatement, Connection connection) {

		try {
			if (resultSet != null) {
				resultSet.close();
			}
		} catch (SQLException exp) {

		} finally {
			try {
				if (pstatement != null) {
					pstatement.close();
				}
			} catch (SQLException exp) {

			} finally {
				try {
					if (connection != null) {
						connection.close();
					}
				} catch (SQLException exp) {

				}

			}
		}
	}
}