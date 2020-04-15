package com.reactiveworks.iplrestservice.deliveries.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.reactiveworks.iplrestservice.db.DBUtil;
import com.reactiveworks.iplrestservice.db.exceptions.DBOperationFailureException;
import com.reactiveworks.iplrestservice.db.exceptions.DataBaseAccessException;
import com.reactiveworks.iplrestservice.db.exceptions.OperationNotSupportedException;
import com.reactiveworks.iplrestservice.deliveries.dao.IDeliveriesDao;
import com.reactiveworks.iplrestservice.model.Delivery;

/**
 * mysql implementation of I
 */
public class DeliveriesDaoMysqlImpl implements IDeliveriesDao {
	private static final Logger LOGGER_OBJ = Logger.getLogger(DeliveriesDaoMysqlImpl.class);
	private static final String INSERT_QUERY = "INSERT INTO deliveries(matchId ,inning ,battingTeam,bowlingTeam,overs,ball,batsMan,bowler,wideRuns,byeRuns ,legbyeRuns,noballRuns ,penaltyRuns,batsmanRuns,extraRuns ,totalRuns) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
	private static final String UPDATE_QUERY = "UPDATE deliveries SET inning=? WHERE id=?;";
	private static final String DELETE_QUERY = "DELETE FROM deliveries WHERE id=?;";
	private static final String SELECT_QUERY = "SELECT * FROM deliveries ;";
	private static final String SELECT_BY_ID_QUERY= "SELECT * FROM deliveries WHERE id=?;";
	static int i = 0;

	/**
	 * Gets the delivery by id.
	 * 
	 * @return the delivery with the given id.
	 * @throws DataBaseAccessException     when unable to access the deliveries
	 *                                     database.
	 * @throws DBOperationFailureException when operation on the deliveries database
	 *                                     fails.
	 */
	public Delivery getDelivery(int id) throws DataBaseAccessException, DBOperationFailureException {
		Delivery deliveryObj = new Delivery();
		ResultSet res = null;
		Connection connection = null;
		PreparedStatement statement = null;
		try {

			connection = DBUtil.getdbconnection();
			statement = connection.prepareStatement(SELECT_BY_ID_QUERY);
			statement.setInt(1, id);
			res = statement.executeQuery();
			if (res.next()) {

				deliveryObj.setMatchId(res.getInt(2));
				deliveryObj.setInning(res.getInt(3));
				deliveryObj.setBattingTeam(res.getString(4));
				deliveryObj.setBowlingTeam(res.getString(5));
				deliveryObj.setOver(res.getDouble(6));
				deliveryObj.setBall(res.getInt(7));
				deliveryObj.setBatsman(res.getString(8));
				deliveryObj.setBowler(res.getString(9));
				deliveryObj.setWideRuns(res.getInt(10));
				deliveryObj.setByeRuns(res.getInt(11));
				deliveryObj.setLegbyeRuns(res.getInt(12));
				deliveryObj.setNoballRuns(res.getInt(13));
				deliveryObj.setPenaltyRuns(res.getInt(14));
				deliveryObj.setBatsmanRuns(res.getInt(15));
				deliveryObj.setExtraRuns(res.getInt(16));
				deliveryObj.setTotalRuns(res.getInt(17));
               LOGGER_OBJ.info("delivery object is constructed from the database record");
			}
		} catch (SQLException sqlExp) {
			LOGGER_OBJ.error("unable to access the deliveries database");
			throw new DataBaseAccessException("unable to access delivery ", sqlExp);
		}

		return deliveryObj;
	}

	/**
	 * Gets the list of deliveries from the deliveries database.
	 * 
	 * @return the list of deliveries.
	 * @throws DataBaseAccessException     when unable to access the deliveries
	 *                                     database.
	 * @throws DBOperationFailureException when operation on the deliveries database
	 *                                     fails.
	 */
	@Override
	public List<Delivery> getDeliveries() throws DataBaseAccessException, DBOperationFailureException {

		LOGGER_OBJ.debug("execution of getDeliveries() started");
		List<Delivery> deliveriesList = new ArrayList<Delivery>();
		ResultSet res = null;
		Connection connection = null;
		PreparedStatement statement = null;
		Delivery deliveryObj = null;
		try {

			connection = DBUtil.getdbconnection();
			statement = connection.prepareStatement(SELECT_QUERY);
			res = statement.executeQuery();
			while (res.next()) {
				deliveryObj = new Delivery();
				deliveryObj.setMatchId(res.getInt(2));
				deliveryObj.setInning(res.getInt(3));
				deliveryObj.setBattingTeam(res.getString(4));
				deliveryObj.setBowlingTeam(res.getString(5));
				deliveryObj.setOver(res.getDouble(6));
				deliveryObj.setBall(res.getInt(7));
				deliveryObj.setBatsman(res.getString(8));
				deliveryObj.setBowler(res.getString(9));
				deliveryObj.setWideRuns(res.getInt(10));
				deliveryObj.setByeRuns(res.getInt(11));
				deliveryObj.setLegbyeRuns(res.getInt(12));
				deliveryObj.setNoballRuns(res.getInt(13));
				deliveryObj.setPenaltyRuns(res.getInt(14));
				deliveryObj.setBatsmanRuns(res.getInt(15));
				deliveryObj.setExtraRuns(res.getInt(16));
				deliveryObj.setTotalRuns(res.getInt(17));
				LOGGER_OBJ.info("adding delivery object with matchId"+ res.getInt(2)+" into the list");
				deliveriesList.add(deliveryObj);

			}
		} catch (SQLException sqlExp) {
			LOGGER_OBJ.error("unable to access the deliveries database");
			throw new DataBaseAccessException("unable to access delivery with id " + deliveryObj.getMatchId(), sqlExp);
		}
		LOGGER_OBJ.debug("execution of getDeliveries() completed");
		return deliveriesList;
	}

	/**
	 * Inserts deliveries into the database.
	 * 
	 * @param deliveries list of deliveries to be inserted into the database.
	 * @throws OperationNotSupportedException when operation is not supported by the
	 *                                        database.
	 * @throws DataBaseAccessException        when unable to access the database.
	 * @throws DBOperationFailureException    when operation on the database fails.
	 */
	@Override
	public void insertDeliveries(List<Delivery> deliveries)
			throws OperationNotSupportedException, DataBaseAccessException, DBOperationFailureException {
		LOGGER_OBJ.debug("execution of insertDeliveries() started");

		try (Connection connection = DBUtil.getdbconnection();
				PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
			connection.setAutoCommit(false);
			deliveries.stream().forEach(delivery -> {
				try {
					insertDelivery(delivery, connection, statement);
                    
				} catch (DataBaseAccessException | DBOperationFailureException e) {
					LOGGER_OBJ.error(e.getMessage());
					e.printStackTrace();
				}
			});
		} catch (SQLException exp) {
			LOGGER_OBJ.error("unable to access database");
			throw new DataBaseAccessException("unable to insert delivery with id into the database", exp);
		}

	}

	/**
	 * Deletes the given record from the database.
	 * 
	 * @param delivery delivery to be deleted from the database.
	 * @throws OperationNotSupportedException when operation is not supported by the
	 *                                        database.
	 * @throws DBOperationFailureException    when operation on the database fails.
	 * @throws DataBaseAccessException        when unable to access the database.
	 */
	@Override
	public void deleteDelivery(int id)
			throws OperationNotSupportedException, DataBaseAccessException, DBOperationFailureException {
		LOGGER_OBJ.debug("execution of deleteDelivery() completed");

		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DBUtil.getdbconnection();
			statement = connection.prepareStatement(DELETE_QUERY);
			statement.setInt(1, id);
			statement.executeUpdate();
			LOGGER_OBJ.info("delivery with id "+id+" is deleted from the database");
		} catch (SQLException exp) {
			LOGGER_OBJ.error("unable to access the database");
			throw new DataBaseAccessException("unable to delete delivery with id " + id, exp);
		} finally {

			DBUtil.cleanupdbresources(null, statement, connection);

		}

		LOGGER_OBJ.debug("execution of deleteDelivery() completed");

	}

	/**
	 * Updates the record in the database.
	 * 
	 * @param delivery delivery to be updated.
	 * @param id       id of the delivery to be updated.
	 * @throws OperationNotSupportedException when operation is not supported by the
	 *                                        database.
	 * @throws DBOperationFailureException    when operation on the database fails.
	 * @throws DataBaseAccessException        when unable to access the database.
	 */
	@Override
	public void updateDelivery(Delivery delivery, int id)
			throws OperationNotSupportedException, DataBaseAccessException, DBOperationFailureException {
		LOGGER_OBJ.debug("execution of updateDelivery() completed");
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DBUtil.getdbconnection();
			statement = connection.prepareStatement(UPDATE_QUERY);
			statement.setInt(1, delivery.getInning());
			statement.setInt(2, id);
			statement.executeUpdate();
			LOGGER_OBJ.info("delivery with id "+id+" is deleted from the database");
		} catch (SQLException exp) {
			LOGGER_OBJ.error("unable to access  database");
			throw new DataBaseAccessException("unable to update delivery with id " + delivery.getMatchId(), exp);
		} finally {

			DBUtil.cleanupdbresources(null, statement, connection);

		}
		LOGGER_OBJ.debug("execution of updateDelivery() completed");
	}

	/**
	 * Inserts delivery into the deliveries database.
	 * 
	 * @param delivery delivery object to be inserted into the deliveries database.
	 * @throws DBOperationFailureException when operation on the database fails.
	 * @throws DataBaseAccessException     when unable to access the database.
	 */
	private void insertDelivery(Delivery delivery, Connection connection, PreparedStatement statement)
			throws DataBaseAccessException, DBOperationFailureException {

		LOGGER_OBJ.debug("execution of insertDelivery() started");

		try {
			i++;

			statement.setInt(1, delivery.getMatchId());
			statement.setInt(2, delivery.getInning());
			statement.setString(3, delivery.getBattingTeam());
			statement.setString(4, delivery.getBowlingTeam());
			statement.setDouble(5, delivery.getOver());
			statement.setInt(6, delivery.getBall());
			statement.setString(7, delivery.getBatsman());
			statement.setString(8, delivery.getBowler());
			statement.setInt(9, delivery.getWideRuns());
			statement.setInt(10, delivery.getByeRuns());
			statement.setInt(11, delivery.getLegbyeRuns());
			statement.setInt(12, delivery.getNoballRuns());
			statement.setInt(13, delivery.getPenaltyRuns());
			statement.setInt(14, delivery.getBatsmanRuns());
			statement.setInt(15, delivery.getExtraRuns());
			statement.setInt(16, delivery.getTotalRuns());
			LOGGER_OBJ.info("adding delivery with matchId "+delivery.getMatchId()+" into the batch");
			statement.addBatch();
			if (i % 2000 == 0) {
				statement.executeBatch();
				LOGGER_OBJ.info("batch insertion of deliveries completed successfully");
				connection.commit();
			}

			// statement.executeUpdate();

		} catch (SQLException exp) {
			LOGGER_OBJ.error("unable to access database");
			throw new DataBaseAccessException(
					"unable to insert product with id " + delivery.getMatchId() + " into the database", exp);
		}
		LOGGER_OBJ.debug("execution of insertDelivery() completed");
	}

	/**
	 * inserts the delivery into the database.
	 * 
	 * @param delivery delivery object to be inserted into the database.
	 * @throws DataBaseAccessException     when unable to access the database.
	 * @throws DBOperationFailureException when database operation fails.
	 */
	public void insertDelivery(Delivery delivery) throws DataBaseAccessException, DBOperationFailureException {

		LOGGER_OBJ.debug("execution of insertDelivery() started");

		Connection connection = null;
		PreparedStatement statement = null;
		try {

			connection = DBUtil.getdbconnection();
			statement = connection.prepareStatement(INSERT_QUERY);

			statement.setInt(1, delivery.getMatchId());
			statement.setInt(2, delivery.getInning());
			statement.setString(3, delivery.getBattingTeam());
			statement.setString(4, delivery.getBowlingTeam());
			statement.setDouble(5, delivery.getOver());
			statement.setInt(6, delivery.getBall());
			statement.setString(7, delivery.getBatsman());
			statement.setString(8, delivery.getBowler());
			statement.setInt(9, delivery.getWideRuns());
			statement.setInt(10, delivery.getByeRuns());
			statement.setInt(11, delivery.getLegbyeRuns());
			statement.setInt(12, delivery.getNoballRuns());
			statement.setInt(13, delivery.getPenaltyRuns());
			statement.setInt(14, delivery.getBatsmanRuns());
			statement.setInt(15, delivery.getExtraRuns());
			statement.setInt(16, delivery.getTotalRuns());
			statement.executeQuery();
			LOGGER_OBJ.info("delivery with matchId "+delivery.getMatchId()+" is inserted into the database");

		} catch (SQLException exp) {
			LOGGER_OBJ.error("unable to access database");
			throw new DataBaseAccessException("unable to insert delivery into the database", exp);
		} finally {
			DBUtil.cleanupdbresources(null, statement, connection);
		}
		LOGGER_OBJ.debug("execution of insertDelivery() completed");
	}

}