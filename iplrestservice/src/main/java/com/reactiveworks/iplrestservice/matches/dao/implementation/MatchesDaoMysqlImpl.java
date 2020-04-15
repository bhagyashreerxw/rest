package com.reactiveworks.iplrestservice.matches.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.reactiveworks.iplrestservice.db.DBUtil;
import com.reactiveworks.iplrestservice.db.exceptions.DBOperationFailureException;
import com.reactiveworks.iplrestservice.db.exceptions.DataBaseAccessException;
import com.reactiveworks.iplrestservice.db.exceptions.OperationNotSupportedException;
import com.reactiveworks.iplrestservice.matches.dao.IMatchesDao;
import com.reactiveworks.iplrestservice.model.Match;

/**
 * mysql implemention of matchesDao.
 */
public class MatchesDaoMysqlImpl implements IMatchesDao {
	private static final Logger LOGGER_OBJ = Logger.getLogger(MatchesDaoMysqlImpl.class);
	private static final String INSERT_QUERY = "INSERT INTO matches VALUES(?,?,?,?,?,?,?,?,?,?);";
	private static final String UPDATE_QUERY = "UPDATE matches SET season=? WHERE matchId=?;";
	private static final String DELETE_QUERY = "DELETE FROM matches WHERE matchId=?;";
	private static final String SELECT_QUERY = "SELECT * FROM matches ;";
	private static final String SELECT_BY_ID_QUERY = "SELECT * FROM matches WHERE matchId=?;";
	private static int i = 0;

	/**
	 * Gets the list of matches from the database.
	 * 
	 * @return the list of matches from the database.
	 * @throws DataBaseAccessException         when unable to access the database.
	 * @throws DBOperationFailureExceptionwhen database operation fails.
	 */
	@Override
	public List<Match> getMatches() throws DataBaseAccessException, DBOperationFailureException {
		LOGGER_OBJ.debug("execution of getDeliveries() started");
		List<Match> matchesList = new ArrayList<Match>();
		ResultSet res = null;
		Connection connection = null;
		Match matchObj = null;
		PreparedStatement statement = null;

		try {
			connection = DBUtil.getdbconnection();
			statement = connection.prepareStatement(SELECT_QUERY);
			res = statement.executeQuery();
			while (res.next()) {
				matchObj = new Match();
				matchObj.setMatchId(res.getInt(1));
				matchObj.setSeason(res.getInt(2));
				matchObj.setCity(res.getString(3));
				Date date = new Date(res.getDate(4).getTime());
				matchObj.setDate(date);
				matchObj.setTeam1(res.getString(5));
				matchObj.setTeam2(res.getString(6));
				matchObj.setTossWinner(res.getString(7));
				matchObj.setTossDecision(res.getString(8));
				matchObj.setResult(res.getString(9));
				matchObj.setWinner(res.getString(10));
				LOGGER_OBJ.info("adding match object with matchId" + res.getInt(2) + " into the list");
				matchesList.add(matchObj);

			}
		} catch (SQLException sqlExp) {
			LOGGER_OBJ.error("unable to access the deliveries database");
			throw new DataBaseAccessException("unable to access delivery with id " + matchObj.getMatchId(), sqlExp);
		} finally {
			DBUtil.cleanupdbresources(res, statement, connection);
		}
		LOGGER_OBJ.debug("execution of getDeliveries() completed");
		return matchesList;

	}

	/**
	 * Inserts matches into the database.
	 * 
	 * @param matches list of matches to be inserted into the database.
	 * @throws OperationNotSupportedException when operation is not supported by the
	 *                                        database.
	 * @throws DataBaseAccessException        when unable to access the database.
	 * @throws DBOperationFailureException    when operation on the database fails.
	 */
	@Override
	public void insertMatches(List<Match> matches)
			throws OperationNotSupportedException, DataBaseAccessException, DBOperationFailureException {
		LOGGER_OBJ.debug("execution of insertMatches() started");

		try (Connection connection = DBUtil.getdbconnection();
				PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
			int size = matches.size();
			connection.setAutoCommit(false);
			matches.stream().forEach(match -> {

				try {
					insertMatch(match, connection, statement, size);
				} catch (DataBaseAccessException | DBOperationFailureException exp) {
					LOGGER_OBJ.error(exp.getMessage());
					exp.printStackTrace();
				}

			});
			connection.setAutoCommit(true);
		} catch (SQLException exp) {
			LOGGER_OBJ.error("unable to access database");
			throw new DataBaseAccessException("unable to insert match with id into the database", exp);
		}

		LOGGER_OBJ.debug("execution of insertMatches() completed");

	}

	/**
	 * Deletes the given record from the database.
	 * 
	 * @param match match to be deleted from the database.
	 * @throws OperationNotSupportedException when operation is not supported by the
	 *                                        database.
	 */
	@Override
	public void deleteMatch(int id)
			throws OperationNotSupportedException, DataBaseAccessException, DBOperationFailureException {
		LOGGER_OBJ.debug("execution of deleteMatch() started");
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DBUtil.getdbconnection();
			statement = connection.prepareStatement(DELETE_QUERY);
			statement.setInt(1, id);
			statement.executeUpdate();
			LOGGER_OBJ.info("match object is with id" + id + " is deleted from the database ");
		} catch (SQLException exp) {
			LOGGER_OBJ.error("unable to access the database");
			throw new DataBaseAccessException("unable to delete delivery with id " + id, exp);
		} finally {

			DBUtil.cleanupdbresources(null, statement, connection);

		}
		LOGGER_OBJ.debug("execution of deleteMatch() completed");

	}

	/**
	 * Updates the record in the database.
	 * 
	 * @param match match to be updated.
	 * @param id    id of the match to be updated.
	 * @throws OperationNotSupportedException when operation is not supported by the
	 *                                        database.
	 */
	@Override
	public void updateMatch(Match match, int id)
			throws OperationNotSupportedException, DataBaseAccessException, DBOperationFailureException {
		LOGGER_OBJ.debug("execution of updateMatch() completed");
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DBUtil.getdbconnection();
			statement = connection.prepareStatement(UPDATE_QUERY);
			statement.setInt(1, match.getSeason());

			statement.setInt(2, id);
			statement.executeUpdate();
			LOGGER_OBJ.info("match object with id" + id + " is updated ");
		} catch (SQLException exp) {
			LOGGER_OBJ.error("unable to access  database");
			throw new DataBaseAccessException("unable to update match with id " + match.getMatchId(), exp);
		} finally {

			DBUtil.cleanupdbresources(null, statement, connection);

		}
		LOGGER_OBJ.debug("execution of updateMatch() completed");

	}

	/**
	 * Inserts match into the database batch wise.
	 * 
	 * @param match match to be inserted into the database.
	 * @throws DataBaseAccessException     when unable to access the database.
	 * @throws DBOperationFailureException when operation on the database fails.
	 */
	private void insertMatch(Match match, Connection connection, PreparedStatement statement, int size)
			throws DataBaseAccessException, DBOperationFailureException {
		LOGGER_OBJ.debug("execution of insertMatch() started");

		try {
			i++;
			statement.setInt(1, match.getMatchId());
			statement.setInt(2, match.getMatchId());
			statement.setString(3, match.getCity());
			java.sql.Date date = new java.sql.Date(match.getDate().getTime());
			statement.setDate(4, date);
			statement.setString(5, match.getTeam1());
			statement.setString(6, match.getTeam2());
			statement.setString(7, match.getTossWinner());
			statement.setString(8, match.getTossDecision());
			statement.setString(9, match.getResult());
			statement.setString(10, match.getWinner());
			statement.addBatch();
			if (i % 10 == 0 || i == size) {
				statement.executeBatch();
				connection.commit();

			}

		} catch (SQLException exp) {
			LOGGER_OBJ.error("unable to access database");
			throw new DataBaseAccessException(
					"unable to insert match with id " + match.getMatchId() + " into the database", exp);
		}
		LOGGER_OBJ.debug("execution of insertMatch() completed");
	}

	/**
	 * Inserts match into the database.
	 * 
	 * @param id    id at which the match has to be inserted.
	 * @param match match to be inserted into the database.
	 * @throws DataBaseAccessException     when unable to access the database.
	 * @throws DBOperationFailureException when operation on the database fails.
	 */
	public void insertMatch(Match match, int id) throws DataBaseAccessException, DBOperationFailureException {
		LOGGER_OBJ.debug("execution of insertMatch() started");
		ResultSet resultSet = null;
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DBUtil.getdbconnection();

			statement = connection.prepareStatement(INSERT_QUERY);
			statement.setInt(1, match.getMatchId());
			statement.setInt(2, match.getMatchId());
			statement.setString(3, match.getCity());
			java.sql.Date date = new java.sql.Date(match.getDate().getTime());
			statement.setDate(4, date);
			statement.setString(5, match.getTeam1());
			statement.setString(6, match.getTeam2());
			statement.setString(7, match.getTossWinner());
			statement.setString(8, match.getTossDecision());
			statement.setString(9, match.getResult());
			statement.setString(10, match.getWinner());
			statement.executeUpdate();

		} catch (SQLException exp) {
			LOGGER_OBJ.error("unable to access database");
			throw new DataBaseAccessException(
					"unable to insert match with id " + match.getMatchId() + " into the database", exp);
		} finally {

			DBUtil.cleanupdbresources(resultSet, statement, connection);

		}
		LOGGER_OBJ.debug("execution of insertMatch() completed");
	}

	/**
	 * gets match from the database with the given id .
	 * 
	 * @param id id of the match to be fetched from the database.
	 * @throws DataBaseAccessException     when unable to access the database.
	 * @throws DBOperationFailureException when operation on the database fails.
	 */
	@Override
	public Match getMatch(int id) throws DataBaseAccessException, DBOperationFailureException {
		LOGGER_OBJ.debug("execution of getMatch() started");
		ResultSet resultSet = null;
		Match matchObj = new Match();
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DBUtil.getdbconnection();
			statement = connection.prepareStatement(SELECT_BY_ID_QUERY);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				matchObj.setMatchId(resultSet.getInt(1));
				matchObj.setSeason(resultSet.getInt(2));
				matchObj.setCity(resultSet.getString(3));
				Date date = new Date(resultSet.getDate(4).getTime());
				matchObj.setDate(date);
				matchObj.setTeam1(resultSet.getString(5));
				matchObj.setTeam2(resultSet.getString(6));
				matchObj.setTossWinner(resultSet.getString(7));
				matchObj.setTossDecision(resultSet.getString(8));
				matchObj.setResult(resultSet.getString(9));
				matchObj.setWinner(resultSet.getString(10));

			}

		} catch (SQLException exp) {
			LOGGER_OBJ.error("unable to access database");
			throw new DataBaseAccessException(
					"unable to get match with id " + matchObj.getMatchId() + " from the database", exp);
		} finally {

			DBUtil.cleanupdbresources(resultSet, statement, connection);

		}
		LOGGER_OBJ.debug("execution of getMatch() completed");
		return matchObj;

	}

	/**
	 * Deletes all records from the database.
	 * 
	 * @throws DataBaseAccessException     when unable to access the database.
	 * @throws DBOperationFailureException when operation on the database fails.
	 */
	public void deleteMatches() throws DataBaseAccessException, DBOperationFailureException {
		LOGGER_OBJ.debug("execution of deleteMatch() started");
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = DBUtil.getdbconnection();
			statement = connection.prepareStatement("DELETE FROM matches;");
			statement.executeQuery();
		} catch (SQLException exp) {
			LOGGER_OBJ.error("unable to delete delivery from database");
			throw new DataBaseAccessException("unable to delete deliveries from the database.",exp);
		} finally {
			DBUtil.cleanupdbresources(null, statement, connection);
		}
		LOGGER_OBJ.debug("execution of deleteMatch() completed");
	}
}