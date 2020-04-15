package com.reactiveworks.iplrestservice.matches.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.reactiveworks.iplrestservice.db.exceptions.DBOperationFailureException;
import com.reactiveworks.iplrestservice.matches.dao.implementation.MatchesDaoCSVImpl;
import com.reactiveworks.iplrestservice.matches.dao.implementation.MatchesDaoMysqlImpl;


public class MatchesDaoFactory {
	
	private static final String PROPERTY_FILE = "dbtype.properties";
	private static final Logger LOGGER_OBJ = Logger.getLogger(MatchesDaoFactory.class);
	private static final String DB_TYPE = "dbtype";
	private static final String CSV = "csv";
	private static final String MYSQL = "mysql";

	public static Properties properties = null;

	/**
	 * Creates the object MatchesDao implementation class.
	 * 
	 * @return the MatchesDao implementation class object.
	 * @throws DBOerationFailureException when operation on the database fails.
	 */
	public static IMatchesDao getInstance() throws DBOperationFailureException {

		IMatchesDao matchesDaoObj = null;
		if (properties == null) {
			try (InputStream input = MatchesDaoFactory.class.getClassLoader().getResourceAsStream(PROPERTY_FILE)) {

				properties = new Properties();
				properties.load(input);

			} catch (IOException exp) {
				LOGGER_OBJ.error("not able to read the properties file " + PROPERTY_FILE);
				throw new DBOperationFailureException("not able to read the file " + PROPERTY_FILE, exp);
			}
		}
		if (((String) properties.get(DB_TYPE)).equalsIgnoreCase(CSV)) {
			matchesDaoObj = new MatchesDaoCSVImpl();
			;
		} else if (((String) properties.get(DB_TYPE)).equalsIgnoreCase(MYSQL)) {
			matchesDaoObj = new MatchesDaoMysqlImpl();
		} else {
			LOGGER_OBJ.debug(properties.get(DB_TYPE) + " implementation does not exist.");
			matchesDaoObj = new MatchesDaoCSVImpl(); // default stockTrade Dao Object
		}
		LOGGER_OBJ.debug("execution of getInstance() completed");
		return matchesDaoObj;
	}

	/**
	 * Creates the object MatchesDao implementation class.
	 * 
	 * @return the MatchesDao implementation class object.
	 * @throws DBOerationFailureException when operation on the database fails.
	 */
	public static IMatchesDao getInstance(String dbType) throws DBOperationFailureException {
		LOGGER_OBJ.debug("execution of getInstance() started");
		IMatchesDao matchesDaoObj = null;

		if (dbType != null) {
			if (dbType.equalsIgnoreCase(CSV)) {
				matchesDaoObj = new MatchesDaoCSVImpl();
			} else if (dbType.equalsIgnoreCase(MYSQL)) {
				matchesDaoObj = new MatchesDaoMysqlImpl();
			} else {
				LOGGER_OBJ.debug(dbType + " implementation does not exist.");
				matchesDaoObj = new MatchesDaoCSVImpl(); // default stockTrade Dao Object
			}
		}

		LOGGER_OBJ.debug("execution of getInstance() completed");
		return matchesDaoObj;
	}
}
