package com.reactiveworks.iplrestservice.controllers;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.reactiveworks.iplrestservice.db.exceptions.DBOperationFailureException;
import com.reactiveworks.iplrestservice.db.exceptions.DataBaseAccessException;
import com.reactiveworks.iplrestservice.db.exceptions.OperationNotSupportedException;
import com.reactiveworks.iplrestservice.deliveries.dao.DeliveriesDaoFactory;
import com.reactiveworks.iplrestservice.deliveries.dao.IDeliveriesDao;
import com.reactiveworks.iplrestservice.model.Delivery;
import com.reactiveworks.iplrestservice.resources.exceptions.DeliveryResourceNotFoundException;
import com.reactiveworks.iplrestservice.resources.exceptions.MatchResourceNotFoundException;
import com.reactiveworks.iplrestservice.resources.exceptions.ResourceServiceFailureException;

/**
 * controller class for the match resource.
 */
@Path("deliveries")
public class DeliveryResourceController {

	private static Logger LOGGER_OBJ = Logger.getLogger(DeliveryResourceController.class);

	/**
	 * method for getting deliveries list.
	 * 
	 * @return the list of deliveries.
	 * @throws ResourceServiceFailureException when resource service fails.
	 * @throws MatchResourceNotFoundException  when the record is not found.
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getDeliveries() throws ResourceServiceFailureException, DeliveryResourceNotFoundException {
		List<Delivery> deliveries;
		try {
			IDeliveriesDao deliveriesDao = DeliveriesDaoFactory.getInstance();
			deliveries = deliveriesDao.getDeliveries();
			if (deliveries.size() == 0) {
				LOGGER_OBJ.error("deliveries database doesn't contain any records.");
				throw new DeliveryResourceNotFoundException("deliveries resource is not found");

			}
		} catch (DBOperationFailureException | DataBaseAccessException exp) {
			LOGGER_OBJ.error("unable to access the deliveries resource.");
			throw new ResourceServiceFailureException("unable to access the deliveries resource", exp);
		}

		return Response.status(200).entity(deliveries).build();

	}

	/**
	 * method for getting delivery with the particular id.
	 * 
	 * @param id id of the delivery.
	 * @return the delivery with the given id.
	 * @throws ResourceServiceFailureException when unable to access the resource.
	 * @throws MatchResourceNotFoundException  when match with the given id.
	 */
	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getDelivery(@PathParam("id") int id)
			throws ResourceServiceFailureException, DeliveryResourceNotFoundException {

		Delivery delivery;
		try {
			IDeliveriesDao deliveriesDao = DeliveriesDaoFactory.getInstance();
			delivery = deliveriesDao.getDelivery(id);
			if (delivery.getMatchId() == 0) {
				throw new DeliveryResourceNotFoundException("delivery with the given id is not found");
			}

		} catch (DBOperationFailureException exp) {
			LOGGER_OBJ.error("unable to access the deliveries resource.");
			throw new ResourceServiceFailureException(
					"unable to access the deliveries resource due to database operation failure", exp);
		} catch (DataBaseAccessException exp) {
			LOGGER_OBJ.error("unable to access the deliveries resource.");
			throw new ResourceServiceFailureException(
					"unable to access the deliveries resource due to database access failure", exp);
		}

		return Response.status(200).entity(delivery).build();

	}

	/**
	 * creates record in the database.
	 * 
	 * @param deliveries the list of deliveries .
	 * @return Response object which contains status of the operation.
	 * @throws ResourceServiceFailureException when unable to create the record in
	 *                                         the database.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN })
	public Response createDelivery(List<Delivery> deliveries) throws ResourceServiceFailureException {
		try {
			IDeliveriesDao deliveriesDao = DeliveriesDaoFactory.getInstance();
			deliveriesDao.insertDeliveries(deliveries);
		} catch (DBOperationFailureException exp) {
			LOGGER_OBJ.error("unable to create the deliveries records into the database.");
			throw new ResourceServiceFailureException(
					"unable to create the deliveries resource due to database operation failure", exp);
		} catch (OperationNotSupportedException exp) {
			LOGGER_OBJ.error("unable to create the deliveries records into the database.");
			throw new ResourceServiceFailureException(
					"unable to create the deliveries resource because database operation is not supported", exp);
		} catch (DataBaseAccessException exp) {
			LOGGER_OBJ.error("unable to create the deliveries records into the database.");
			throw new ResourceServiceFailureException(
					"unable to create the deliveries resource due to database access failure", exp);
		}

		return Response.status(201).entity("delivery records are created in the database").build();
	}

	/**
	 * updates the record with the given id if exists otherwise creates a new record
	 * in the database.
	 * 
	 * @param id id of the delivery to be updated.
	 * @param delivery delivery object.
	 * @return Response object which contains status of the operation.
	 * @throws ResourceServiceFailureException unable to update the match because
	 *                                         database;
	 */
	@PUT
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateDelivery(@PathParam("id") int id, Delivery delivery) throws ResourceServiceFailureException {
		Delivery deliveryObj;

		try {
			IDeliveriesDao deliveriesDao = DeliveriesDaoFactory.getInstance();
			deliveryObj = deliveriesDao.getDelivery(id);
			if (deliveryObj.getMatchId() == 0) {
				deliveriesDao.insertDelivery(delivery);
			} else {
				deliveriesDao.updateDelivery(delivery, id);
			}
		} catch (DataBaseAccessException exp) {
			LOGGER_OBJ.error("unable to update the delivery.");
			throw new ResourceServiceFailureException(
					"unable to update the delivery  due to database operation failure", exp);
		} catch (DBOperationFailureException exp) {
			LOGGER_OBJ.error("unable to access the deliveries resource.");
			throw new ResourceServiceFailureException("unable to update the delivery due to database operation failure",
					exp);
		} catch (OperationNotSupportedException exp) {
			LOGGER_OBJ.error("unable to update the delivery.");
			throw new ResourceServiceFailureException(
					"unable to update the delivery because database operation is not supported", exp);
		}

		return Response.status(201).entity("delivery record is updated").build();
	}

	/**
	 * deletes the record with the given id.
	 * 
	 * @param id id of the delivery to be deleted.
	 * @return Response object which contains status of the operation.
	 * @throws MatchResourceNotFoundException  when match resource is not found.
	 * @throws ResourceServiceFailureException unable to delete the match.
	 */
	@DELETE
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteDelivery(@PathParam("id") int id)
			throws DeliveryResourceNotFoundException, ResourceServiceFailureException {

		try {

			IDeliveriesDao deliveriesDao = DeliveriesDaoFactory.getInstance();
			if (deliveriesDao.getDelivery(id).getMatchId() == 0) {
				throw new DeliveryResourceNotFoundException("delivery with the given id is not found");
			} else {
				deliveriesDao.deleteDelivery(id);
			}
		} catch (DataBaseAccessException exp) {
			LOGGER_OBJ.error("unable to delete the delivery .");
			throw new ResourceServiceFailureException(
					"unable to delete the delivery  due to database operation failure", exp);
		} catch (DBOperationFailureException exp) {
			LOGGER_OBJ.error("unable to delete the delivery .");
			throw new ResourceServiceFailureException("unable to delete the delivery due to database operation failure",
					exp);
		} catch (OperationNotSupportedException exp) {
			LOGGER_OBJ.error("unable to delete the delivery .");
			throw new ResourceServiceFailureException(
					"unable to delete the delivery because database operation is not supported", exp);
		}

		return Response.status(200).entity("delivery is deleted").build();

	}

	/**
	 * updates the the delivery record.
	 * 
	 * @param id    the id of the delivery to be updated.
	 * @param match match object.
	 * @return Response object which contains status of the operation.
	 * @throws MatchResourceNotFoundException  when match resource is not found.
	 * @throws ResourceServiceFailureException unable to delete the match.
	 */
	@PATCH
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response update(@PathParam("id") int id, Delivery delivery)
			throws ResourceServiceFailureException, DeliveryResourceNotFoundException {
		Delivery deliveryObj;

		try {
			IDeliveriesDao deliveriesDao = DeliveriesDaoFactory.getInstance();
			deliveryObj = deliveriesDao.getDelivery(id);
			System.out.println(deliveryObj);
			if (deliveryObj.getMatchId() == 0) {
				throw new DeliveryResourceNotFoundException("delivery with the given id is not found");
			} else {
				deliveriesDao.updateDelivery(delivery, id);
			}
		} catch (DataBaseAccessException exp) {
			LOGGER_OBJ.error("unable to update the delivery.");
			throw new ResourceServiceFailureException(
					"unable to update the delivery  due to database operation failure", exp);
		} catch (DBOperationFailureException exp) {
			LOGGER_OBJ.error("unable to update the delivery.");
			throw new ResourceServiceFailureException("unable to update the delivery due to database operation failure",
					exp);
		} catch (OperationNotSupportedException exp) {
			LOGGER_OBJ.error("unable to update the delivery.");
			throw new ResourceServiceFailureException(
					"unable to update the delivery because database operation is not supported", exp);
		}

		return Response.status(200).entity("delivery record is updated").build();
	}

}
