package com.reactiveworks.iplrestservice.controllers;

import java.util.ArrayList;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import com.reactiveworks.iplrestservice.db.exceptions.DBOperationFailureException;
import com.reactiveworks.iplrestservice.db.exceptions.DataBaseAccessException;
import com.reactiveworks.iplrestservice.db.exceptions.OperationNotSupportedException;
import com.reactiveworks.iplrestservice.matches.dao.IMatchesDao;
import com.reactiveworks.iplrestservice.matches.dao.MatchesDaoFactory;
import com.reactiveworks.iplrestservice.model.Match;
import com.reactiveworks.iplrestservice.model.PageContent;
import com.reactiveworks.iplrestservice.model.PageLink;
import com.reactiveworks.iplrestservice.resources.exceptions.MatchResourceNotFoundException;
import com.reactiveworks.iplrestservice.resources.exceptions.ResourceServiceFailureException;

/**
 * controller class for the match resource.
 */
@Path("matches")
public class MatchResourceController {

	private static final Logger LOGGER_OBJ = Logger.getLogger(MatchResourceController.class);

	/**
	 * method for getting matches list.
	 * 
	 * @return the list of matches.
	 * @throws ResourceServiceFailureException when resource service fails.
	 * @throws MatchResourceNotFoundException  when the record is not found.
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Response getMatches(@Context UriInfo uriInfo, @QueryParam("start") int start, @QueryParam("size") int size)
			throws ResourceServiceFailureException, MatchResourceNotFoundException {
		List<Match> matches;
		try {
			IMatchesDao matchesDao = MatchesDaoFactory.getInstance();
			matches = matchesDao.getMatches();
			if (matches.size() == 0) {
				LOGGER_OBJ.error("unable to access the match resource.");
				throw new MatchResourceNotFoundException("matches resource is not found");

			}
		} catch (DBOperationFailureException | DataBaseAccessException e) {
			LOGGER_OBJ.error("unable to access the match resource.");
			throw new ResourceServiceFailureException("unable to access the matches resource", e);
		}
		GenericEntity<List<Match>> entity = new GenericEntity<List<Match>>(matches) {
		};
		if (start + size <= matches.size()) {
			if (start >= 0 && size > 0) {
				return createPageContent(uriInfo, start, size, matches);
			}
		}
		
		return Response.status(200).entity(entity).build();

	}

	/**
	 * method for getting match with the particular id.
	 * 
	 * @param id id of the match.
	 * @return the match with the given id.
	 * @throws ResourceServiceFailureException when unable to access the resource.
	 * @throws MatchResourceNotFoundException  when match with the given id.
	 */
	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getMatch(@PathParam("id") int id)
			throws ResourceServiceFailureException, MatchResourceNotFoundException {

		Match match;
		try {
			IMatchesDao matchesDao = MatchesDaoFactory.getInstance();
			match = matchesDao.getMatch(id);
			if (match.getMatchId() == 0) {
				LOGGER_OBJ.error("match resource is not found");
				throw new MatchResourceNotFoundException("match with the given id is not found");
			}

		} catch (DBOperationFailureException e) {
			LOGGER_OBJ.error("unable to access the match resource.");
			throw new ResourceServiceFailureException(
					"unable to access the matches resource due to database operation failure", e);
		} catch (DataBaseAccessException e) {
			LOGGER_OBJ.error("unable to access the match resource.");
			throw new ResourceServiceFailureException(
					"unable to access the matches resource due to database access failure", e);
		}

		return Response.status(200).entity(match).build();

	}

	/**
	 * creates record in the database.
	 * 
	 * @param matches the list of matches .
	 * @return Response object which contains status of the operation.
	 * @throws ResourceServiceFailureException when unable to create the record in
	 *                                         the database.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN })
	public Response createDelivery(List<Match> matches) throws ResourceServiceFailureException {
		try {
			IMatchesDao matchesDao = MatchesDaoFactory.getInstance();
			matchesDao.insertMatches(matches);
		} catch (DBOperationFailureException e) {
			LOGGER_OBJ.error("unable to create the matches in the database.");
			throw new ResourceServiceFailureException(
					"unable to create the matches resource due to database operation failure", e);
		} catch (OperationNotSupportedException e) {
			LOGGER_OBJ.error("unable to create the matches in the database.");
			throw new ResourceServiceFailureException(
					"unable to create the matches resource because database operation is not supported", e);
		} catch (DataBaseAccessException e) {
			LOGGER_OBJ.error("unable to create the matches in the database.");
			throw new ResourceServiceFailureException(
					"unable to create the matches resource due to database access failure", e);
		}

		return Response.status(201).entity("matches records are created in the database").build();
	}

	/**
	 * updates the record with the given id if exists otherwise creates a new record
	 * in the database.
	 * 
	 * @param id    id of the match to be updated.
	 * @param match match object.
	 * @return Response object which contains status of the operation.
	 * @throws ResourceServiceFailureException unable to update the match because
	 *                                         database;
	 */
	@PUT
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateDelivery(@PathParam("id") int id, Match match) throws ResourceServiceFailureException {
		Match matchesObj;

		try {
			IMatchesDao matchesDao = MatchesDaoFactory.getInstance();
			matchesObj = matchesDao.getMatch(id);
			if (matchesObj.getMatchId() == 0) {
				matchesDao.insertMatch(match, id);
			} else {
				matchesDao.updateMatch(match, id);
			}
		} catch (DataBaseAccessException e) {
			LOGGER_OBJ.error("unable to update the match.");
			throw new ResourceServiceFailureException("unable to update the match  due to database operation failure");
		} catch (DBOperationFailureException e) {
			LOGGER_OBJ.error("unable to update the match.");
			throw new ResourceServiceFailureException("unable to update the match due to database operation failure");
		} catch (OperationNotSupportedException e) {
			LOGGER_OBJ.error("unable to update the match.");
			throw new ResourceServiceFailureException(
					"unable to update the match because database operation is not supported");
		}

		return Response.status(201).entity("delivery record is updated").build();
	}

	/**
	 * deletes the record with the given id.
	 * 
	 * @param id id of the match to be deleted.
	 * @return Response object which contains status of the operation.
	 * @throws MatchResourceNotFoundException  when match resource is not found.
	 * @throws ResourceServiceFailureException unable to delete the match.
	 */
	@DELETE
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteDelivery(@PathParam("id") int id)
			throws MatchResourceNotFoundException, ResourceServiceFailureException {

		try {

			IMatchesDao matchesDao = MatchesDaoFactory.getInstance();
			if (matchesDao.getMatch(id).getMatchId() == 0) {
				matchesDao.deleteMatches();
			} else {
				matchesDao.deleteMatch(id);
			}
		} catch (DataBaseAccessException e) {
			LOGGER_OBJ.error("unable to delete the match record.");
			throw new ResourceServiceFailureException("unable to delete the match  due to database operation failure",
					e);
		} catch (DBOperationFailureException e) {
			LOGGER_OBJ.error("unable to delete the match record.");
			throw new ResourceServiceFailureException("unable to delete the match due to database operation failure",
					e);
		} catch (OperationNotSupportedException e) {
			LOGGER_OBJ.error("unable to delete the match record");
			throw new ResourceServiceFailureException(
					"unable to delete the match because database operation is not supported", e);
		}
		

		return Response.status(200).entity("match is deleted").build();

	}

	/**
	 * updates the the match record.
	 * 
	 * @param id    the id of the match to be updated.
	 * @param match match object.
	 * @return Response object which contains status of the operation.
	 * @throws MatchResourceNotFoundException  when match resource is not found.
	 * @throws ResourceServiceFailureException unable to delete the match.
	 */
	@PATCH
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response update(@PathParam("id") int id, Match match)
			throws ResourceServiceFailureException, MatchResourceNotFoundException {
		Match matchObj;

		try {
			IMatchesDao matchesDao = MatchesDaoFactory.getInstance();
			matchObj = matchesDao.getMatch(id);
			System.out.println(matchObj);
			if (matchObj.getMatchId() == 0) {
				LOGGER_OBJ.error("match with the given id is not found");
				throw new MatchResourceNotFoundException("match with the given id is not found");
			} else {
				matchesDao.updateMatch(match, id);
			}
		} catch (DataBaseAccessException e) {
			LOGGER_OBJ.error("unable to update the match record");
			throw new ResourceServiceFailureException("unable to update the match  due to database operation failure",
					e);
		} catch (DBOperationFailureException e) {
			LOGGER_OBJ.error("unable to update the match record");
			throw new ResourceServiceFailureException("unable to update the match due to database operation failure",
					e);
		} catch (OperationNotSupportedException e) {
			LOGGER_OBJ.error("unable to update the match record");
			throw new ResourceServiceFailureException(
					"unable to update the match because database operation is not supported", e);
		}

		return Response.status(200).entity("match record is updated").build();
	}

	/**
	 * paginates the matches data.
	 * 
	 * @param uriInfo uri info for the get operation.
	 * @param start   start index of the content for the display.
	 * @param size    number of records to be printed in one page.
	 * @param matches list of matches.
	 * @return the Response with the page contents.
	 */
	private Response createPageContent(UriInfo uriInfo, int start, int size, List<Match> matches) {

		List<PageContent> pagecontents = new ArrayList<PageContent>();

		for (int i = start; i + size < matches.size(); i += size) {
			PageLink link = new PageLink();
			PageContent content = new PageContent();
			content.setMatches(matches.subList(i, i + size));
			String uri = uriInfo.getBaseUriBuilder().path(MatchResourceController.class).build().toString();
			uri = uri + "?start=" + (start + size) + "&size=" + size;
			link.setNext(uri);
			if (start - size >= 0) {
				uri = uriInfo.getBaseUriBuilder().path(MatchResourceController.class).build().toString();
				uri = uri + "?start=" + (start - size) + "&size=" + size;
				link.setPrev(uri);
			}
			content.setLink(link);
			pagecontents.add(content);
		}
		return Response.status(Status.OK).entity(pagecontents.get(0)).build();
	}

}
