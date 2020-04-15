package com.reactiveworks.iplrestservice.deliveries.dao.implementation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.reactiveworks.iplrestservice.db.exceptions.DBOperationFailureException;
import com.reactiveworks.iplrestservice.db.exceptions.DataBaseAccessException;
import com.reactiveworks.iplrestservice.db.exceptions.InvalidDBRecordFormatException;
import com.reactiveworks.iplrestservice.db.exceptions.OperationNotSupportedException;
import com.reactiveworks.iplrestservice.deliveries.dao.IDeliveriesDao;
import com.reactiveworks.iplrestservice.model.Delivery;

/**
 * csv implementation of the IDeliveriesDao.
 */
public class DeliveriesDaoCSVImpl implements IDeliveriesDao {
	private static final String FILE_NAME = "deliveries.csv";
	private static final Logger LOGGER_OBJ = Logger.getLogger(DeliveriesDaoCSVImpl.class);

	/**
	 * gets the list of deliveries from csv file.
	 * 
	 * @return the list of deliveries.
	 * @throws DataBaseAccessException when unable to access the database.
	 */
	@Override
	public List<Delivery> getDeliveries() throws DataBaseAccessException {
		LOGGER_OBJ.debug("execution of getDeliveries() started.");
		File file = new File(getClass().getClassLoader().getResource(FILE_NAME).getFile());
        LOGGER_OBJ.info("reading stocktrade details from "+FILE_NAME);
		List<Delivery> deliveriesList;

		try (Stream<String> line = Files.lines(Paths.get(file.toURI()))) {

			deliveriesList = line.skip(1).map(lines -> {
				try {
					return parseCSVLine(lines);
				} catch (InvalidDBRecordFormatException exp) {
					LOGGER_OBJ.error("format of db is invalid" + exp);

				}
				return null;
			}).collect(Collectors.toList());

		} catch (IOException ioExp) {
			throw new DataBaseAccessException("unable to accessthe database" + ioExp);
		}
		LOGGER_OBJ.debug("execution of getDeliveries() completed.");
		return deliveriesList;
	}

	/**
	 * Reads each line from the deliveries CSV file and creates a Delivery object.
	 * 
	 * @param lineStr String in the first line of the deliveries CSV file.
	 * @return the Delivery object.
	 * @throws InvalidDBRecordFormatException when format of the db record is
	 *                                        invalid
	 */
	private Delivery parseCSVLine(String lineStr) throws InvalidDBRecordFormatException {
		// LOGGER_OBJ.debug("execution of parseCSVLine() started.");
		Delivery deliveryObj = new Delivery();
		String deliveryDetails[] = lineStr.split(",");

		try {
			int matchId = Integer.parseInt(deliveryDetails[0]);
			deliveryObj.setMatchId(matchId);
			int inning = Integer.parseInt(deliveryDetails[1]);
			deliveryObj.setInning(inning);
			deliveryObj.setBattingTeam(deliveryDetails[2]);
			deliveryObj.setBowlingTeam(deliveryDetails[3]);
			double over = Double.parseDouble(deliveryDetails[4]);
			deliveryObj.setOver(over);
			int ball = Integer.parseInt(deliveryDetails[5]);
			deliveryObj.setBall(ball);
			deliveryObj.setBatsman(deliveryDetails[6]);
			deliveryObj.setBowler(deliveryDetails[7]);
			int wideRuns = Integer.parseInt(deliveryDetails[8]);
			deliveryObj.setWideRuns(wideRuns);
			int setByeRuns = Integer.parseInt(deliveryDetails[9]);
			deliveryObj.setByeRuns(setByeRuns);
			int legByeRuns = Integer.parseInt(deliveryDetails[10]);
			deliveryObj.setLegbyeRuns(legByeRuns);
			int noBallRuns = Integer.parseInt(deliveryDetails[11]);
			deliveryObj.setNoballRuns(noBallRuns);
			int penaltyRuns = Integer.parseInt(deliveryDetails[12]);
			deliveryObj.setPenaltyRuns(penaltyRuns);
			int batsmanRuns = Integer.parseInt(deliveryDetails[13]);
			deliveryObj.setBatsmanRuns(batsmanRuns);
			int extraRuns = Integer.parseInt(deliveryDetails[14]);
			deliveryObj.setExtraRuns(extraRuns);
			int totalRuns = Integer.parseInt(deliveryDetails[15]);
			deliveryObj.setTotalRuns(totalRuns);

		} catch (NumberFormatException numFormatExp) {
			LOGGER_OBJ.error("number format of the delivery details is not valid");
			throw new InvalidDBRecordFormatException("number format of the delivery details is not valid",
					numFormatExp);
		}
		// LOGGER_OBJ.debug("execution of parseCSVLine() completed.");
		return deliveryObj;

	}

	/**
	 * Inserts deliveries into the database.
	 * 
	 * @param deliveries list of deliveries to be inserted into the database.
	 * @throws OperationNotSupportedException when operation is not supported by the
	 *                                        database.
	 */
	@Override
	public void insertDeliveries(List<Delivery> deliveries) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("insert Operation is not supported by database");
	}

	/**
	 * Deletes the given record from the database.
	 * 
	 * @param delivery delivery to be deleted from the database.
	 * @throws OperationNotSupportedException when operation is not supported by the
	 *                                        database.
	 */
	@Override
	public void deleteDelivery(int id) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("delete Operation is not supported by database");

	}

	/**
	 * Updates the record in the database.
	 * 
	 * @param delivery  delivery to be updated.
	 * @param totalRuns updated value of the totalRuns.
	 * @throws OperationNotSupportedException when operation is not supported by the
	 *                                        database.
	 */
	@Override
	public void updateDelivery(Delivery product, int totalRuns) throws OperationNotSupportedException {
		throw new OperationNotSupportedException("update Operation is not supported by database");

	}

	/**
	 * gets the record from the database.
	 * 
	 * @param id id of the delivery to be fetched.
	 * @throws OperationNotSupportedException when operation is not supported by the
	 *                                        database.
	 */
	@Override
	public Delivery getDelivery(int id) throws DataBaseAccessException, DBOperationFailureException {

		return getDeliveries().get(id + 1);
	}

	/**
	 * inserts the record into the database.
	 * 
	 * @param delivery delivery object to be inserted into the database.
	 * @throws OperationNotSupportedException when operation is not supported by the
	 *                                        database.
	 */
	@Override
	public void insertDelivery(Delivery delivery)
			throws DataBaseAccessException, DBOperationFailureException, OperationNotSupportedException {
		throw new OperationNotSupportedException("update Operation is not supported by database");
	}

}