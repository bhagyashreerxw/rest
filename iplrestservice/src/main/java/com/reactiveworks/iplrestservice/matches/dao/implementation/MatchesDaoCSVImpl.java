package com.reactiveworks.iplrestservice.matches.dao.implementation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.reactiveworks.iplrestservice.db.exceptions.DBOperationFailureException;
import com.reactiveworks.iplrestservice.db.exceptions.DataBaseAccessException;
import com.reactiveworks.iplrestservice.db.exceptions.InvalidDBRecordFormatException;
import com.reactiveworks.iplrestservice.db.exceptions.OperationNotSupportedException;
import com.reactiveworks.iplrestservice.matches.dao.IMatchesDao;
import com.reactiveworks.iplrestservice.model.Match;

/**
 * mysql implementation of IMatchesDao.
 */
public class MatchesDaoCSVImpl implements IMatchesDao {

	private static final String FILE_NAME = "matches.csv";
	private static final Logger LOGGER_OBJ = Logger.getLogger(MatchesDaoCSVImpl.class);

	/**
	 * gets the list of matches from the database.
	 * @return the list of matches.
	 * @throws DataBaseAccessException when unable to access the database.
	 */
	@Override
	public List<Match> getMatches() throws DataBaseAccessException {
		LOGGER_OBJ.debug("execution of getMatches() started.");
		File file = new File(getClass().getClassLoader().getResource(FILE_NAME).getFile());

		List<Match> matchesList;

		try (Stream<String> line = Files.lines(Paths.get(file.toURI()))) {

			matchesList = line.skip(1).map(str -> {
				try {
					return parseCSVLine(str);
				} catch (InvalidDBRecordFormatException InvFormatExp) {
					LOGGER_OBJ.error("format of the db record is invalid" + InvFormatExp);
				}
				return null;
			}).collect(Collectors.toList());

		} catch (IOException ioExp) {
			LOGGER_OBJ.error("unable to access the database");
			throw new DataBaseAccessException("unable to access the database" + ioExp);
		}
		LOGGER_OBJ.debug("execution of getMatches() completed.");
		return matchesList;
	}

	/**
	 * Reads each line from the matches CSV file and creates a Match object.
	 * 
	 * @param lineStr String in the first line of the matches CSV file.
	 * @return the Match object.
	 * @throws InvalidDBRecordFormatException
	 * @throws InvalidMatchDetailsException   when the format of the match details
	 *                                        is not valid.
	 */
	private Match parseCSVLine(String lineStr) throws InvalidDBRecordFormatException {
		// LOGGER_OBJ.debug("execution of parseCSVLine() started.");
		Match matchObj = new Match();
		String matchDetails[] = lineStr.split(",");

		try {
			int matchId = Integer.parseInt(matchDetails[0]);
			matchObj.setMatchId(matchId);
			int season = Integer.parseInt(matchDetails[1]);
			matchObj.setSeason(season);
			matchObj.setCity(matchDetails[2]);
			Date date = new SimpleDateFormat("yyyy-mm-dd").parse(matchDetails[3]);
			matchObj.setDate(date);
			matchObj.setTeam1(matchDetails[4]);
			matchObj.setTeam2(matchDetails[5]);
			matchObj.setTossWinner(matchDetails[6]);
			matchObj.setTossDecision(matchDetails[7]);
			matchObj.setResult(matchDetails[8]);
			if (matchDetails.length == 10) {
				matchObj.setWinner(matchDetails[9]);
			}

		} catch (NumberFormatException numFormatExp) {
			LOGGER_OBJ.error("number format of the match details is not valid");
			throw new InvalidDBRecordFormatException(numFormatExp);
		} catch (ParseException parseExp) {
			LOGGER_OBJ.error("date format of the match details is not valid");
			throw new InvalidDBRecordFormatException(parseExp);
		}
		// LOGGER_OBJ.debug("execution of parseCSVLine() completed.");
		return matchObj;

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

		throw new OperationNotSupportedException("insert Operation is not supported by database");
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

		throw new OperationNotSupportedException("delete Operation is not supported by database");
	}

	/**
	 * Updates the record in the database.
	 * 
	 * @param match  match to be updated.
	 * @param season updated value of the season.
	 * @throws OperationNotSupportedException when operation is not supported by the
	 *                                        database.
	 */
	@Override
	public void updateMatch(Match match, int season)
			throws OperationNotSupportedException, DataBaseAccessException, DBOperationFailureException {
		throw new OperationNotSupportedException("update Operation is not supported by database");

	}

	@Override
	public Match getMatch(int id) throws DataBaseAccessException {
		return getMatches().get(id + 1);
	}

	@Override
	public void insertMatch(Match match, int id) throws DataBaseAccessException, DBOperationFailureException, OperationNotSupportedException {
		throw new OperationNotSupportedException("insert Operation is not supported by database");
	}


	@Override
	public void deleteMatches() throws DataBaseAccessException, DBOperationFailureException, OperationNotSupportedException {
	
		throw new OperationNotSupportedException("delete Operation is not supported by database");
	}

}