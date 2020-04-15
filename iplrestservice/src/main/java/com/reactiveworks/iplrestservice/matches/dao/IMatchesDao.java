package com.reactiveworks.iplrestservice.matches.dao;

import java.util.List;

import com.reactiveworks.iplrestservice.db.exceptions.DBOperationFailureException;
import com.reactiveworks.iplrestservice.db.exceptions.DataBaseAccessException;
import com.reactiveworks.iplrestservice.db.exceptions.OperationNotSupportedException;
import com.reactiveworks.iplrestservice.model.Match;

public interface IMatchesDao {

	/**
	 * Gets the list of matches from the database.
	 * 
	 * @return the list of matches from the database.
	 * @throws DataBaseAccessException when unable to access the database.
	 */
	public List<Match> getMatches() throws DataBaseAccessException, DBOperationFailureException;

	/**
	 * Inserts matches into the database.
	 * 
	 * @param matches list of matches to be inserted into the database.
	 * @throws OperationNotSupportedException when operation is not supported by the
	 *                                        database.
	 * @throws DataBaseAccessException        when unable to access the database.
	 * @throws DBOperationFailureException    when operation on the database fails.
	 */
	public void insertMatches(List<Match> matches)
			throws OperationNotSupportedException, DataBaseAccessException, DBOperationFailureException;

	/**
	 * Deletes the given record from the database.
	 * 
	 * @param match match to be deleted from the database.
	 * @throws OperationNotSupportedException when operation is not supported by the
	 *                                        database.
	 */
	public void deleteMatch(int id)
			throws OperationNotSupportedException, DataBaseAccessException, DBOperationFailureException;

	/**
	 * Updates the record in the database.
	 * 
	 * @param match  match to be updated.
	 * @param season updated value of the season.
	 * @throws OperationNotSupportedException when operation is not supported by the
	 *                                        database.
	 */
	public void updateMatch(Match match, int id)
			throws OperationNotSupportedException, DataBaseAccessException, DBOperationFailureException;

	/**
	 * gets the match with given id from the database.
	 * 
	 * @param id id of the match to be fetched from the database.
	 * @return the match with the given id.
	 * @throws DataBaseAccessException     when unable to access the database.
	 * @throws DBOperationFailureException when operation on the database fails.
	 */
	public Match getMatch(int id) throws DataBaseAccessException, DBOperationFailureException;

	/**
	 * inserts match into the database.
	 * @param match the match object to be inserted into the database.
	 * @throws DataBaseAccessException     when unable to access the database.
	 * @throws DBOperationFailureException when operation on the database fails.
	 */
	public void insertMatch(Match match, int id) throws DataBaseAccessException, DBOperationFailureException,OperationNotSupportedException;
	public void deleteMatches() throws DataBaseAccessException, DBOperationFailureException,OperationNotSupportedException;
}
