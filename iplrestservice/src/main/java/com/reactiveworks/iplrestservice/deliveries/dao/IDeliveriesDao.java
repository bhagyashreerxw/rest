package com.reactiveworks.iplrestservice.deliveries.dao;

import java.util.List;

import com.reactiveworks.iplrestservice.db.exceptions.DBOperationFailureException;
import com.reactiveworks.iplrestservice.db.exceptions.DataBaseAccessException;
import com.reactiveworks.iplrestservice.db.exceptions.OperationNotSupportedException;
import com.reactiveworks.iplrestservice.model.Delivery;

public interface IDeliveriesDao {
	
	public void insertDelivery(Delivery delivery) throws DataBaseAccessException, DBOperationFailureException, OperationNotSupportedException ;

	
	public Delivery getDelivery(int id) throws DataBaseAccessException, DBOperationFailureException;

	/**
	 * Gets the list of deliveries from the database.
	 * 
	 * @return the list of deliveries from the database.
	 * @throws DataBaseAccessException when unable to access the database.
	 */
	public List<Delivery> getDeliveries() throws DataBaseAccessException, DBOperationFailureException ;

	/**
	 * Inserts deliveries into the database.
	 * 
	 * @param deliveries list of deliveries to be inserted into the database.
	 * @throws OperationNotSupportedException when operation is not supported by the
	 *                                        database.
	 * @throws DataBaseAccessException        when unable to access the database.
	 * @throws DBOperationFailureException    when operation on the database fails.
	 */
	public void insertDeliveries(List<Delivery> deliveries)
			throws OperationNotSupportedException, DataBaseAccessException, DBOperationFailureException;

	/**
	 * Deletes the given record from the database.
	 * 
	 * @param delivery delivery to be deleted from the database.
	 * @throws OperationNotSupportedException when operation is not supported by the
	 *                                        database.
	 */
	public void deleteDelivery(int id)
			throws OperationNotSupportedException, DataBaseAccessException, DBOperationFailureException;

	/**
	 * Updates the record in the database.
	 * 
	 * @param delivery  delivery to be updated.
	 * @param totalRuns updated value of the totalRuns.
	 * @throws OperationNotSupportedException when operation is not supported by the
	 *                                        database.
	 */
	public void updateDelivery(Delivery product, int totalRuns)
			throws OperationNotSupportedException, DataBaseAccessException, DBOperationFailureException;

}