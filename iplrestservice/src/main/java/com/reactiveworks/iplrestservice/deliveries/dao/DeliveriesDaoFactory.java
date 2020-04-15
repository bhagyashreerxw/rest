package com.reactiveworks.iplrestservice.deliveries.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.reactiveworks.iplrestservice.db.exceptions.DBOperationFailureException;
import com.reactiveworks.iplrestservice.deliveries.dao.implementation.DeliveriesDaoCSVImpl;
import com.reactiveworks.iplrestservice.deliveries.dao.implementation.DeliveriesDaoMysqlImpl;
import com.reactiveworks.iplrestservice.matches.dao.MatchesDaoFactory;

/**
 * Dao factory for Deliveries Dao.
 */
public class DeliveriesDaoFactory {

	private static final String PROPERTY_FILE = "dbtype.properties";
	private static final Logger LOGGER_OBJ = Logger.getLogger(DeliveriesDaoFactory.class);
	private static final String DB_TYPE = "dbtype";
	private static final String CSV = "csv";
	private static final String MYSQL = "mysql";

	public static Properties properties = null;

	/**
	 * Creates the object DeliveriesDao implementation class.
	 * 
	 * @return the DeliveriesDao implementation class object.
	 * @throws DBOerationFailureException when operation on the database fails.
	 */
	public static IDeliveriesDao getInstance() throws DBOperationFailureException {

		IDeliveriesDao deliveriesDaoObj = null;
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
			deliveriesDaoObj = new DeliveriesDaoCSVImpl();
			;
		} else if (((String) properties.get(DB_TYPE)).equalsIgnoreCase(MYSQL)) {
			deliveriesDaoObj = new DeliveriesDaoMysqlImpl();
		} else {
			LOGGER_OBJ.debug(properties.get(DB_TYPE) + " implementation does not exist.");
			deliveriesDaoObj = new DeliveriesDaoCSVImpl(); // default stockTrade Dao Object
		}
		LOGGER_OBJ.debug("execution of getInstance() completed");
		return deliveriesDaoObj;
	}// ..end of the metod 

	/**
	 * Creates the object DeliveriesDao implementation class.
	 * 
	 * @return the DeliveriesDao implementation class object.
	 * @throws DBOerationFailureException when operation on the database fails.
	 */
	public static IDeliveriesDao getInstance(String dbType) throws DBOperationFailureException {
		LOGGER_OBJ.debug("execution of getInstance() started");
		IDeliveriesDao deliveriesDaoObj = null;

		if (dbType != null) {
			if (dbType.equalsIgnoreCase(CSV)) {
				deliveriesDaoObj = new DeliveriesDaoCSVImpl();
			} else if (dbType.equalsIgnoreCase(MYSQL)) {
				deliveriesDaoObj = new DeliveriesDaoMysqlImpl();
			} else {
				LOGGER_OBJ.info(dbType + " implementation does not exist.");
				deliveriesDaoObj = new DeliveriesDaoCSVImpl(); // default deliveries Dao Object
			}
		}

		LOGGER_OBJ.debug("execution of getInstance() completed");
		return deliveriesDaoObj;
	}

}
