package it.sturrini.gamesite.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.sturrini.common.exception.GamesiteException;
import it.sturrini.gamesite.api.conf.BaseResource;
import it.sturrini.gamesite.api.conf.BaseResponse;
import it.sturrini.gamesite.api.conf.ResourceUtil;
import it.sturrini.gamesite.controllers.PlayerActionsController;
import it.sturrini.gamesite.model.actions.MoveMapElementAction;
import it.sturrini.gamesite.model.actions.ResearchAction;

@Path("/player-action/{id}")
@Consumes(MediaType.APPLICATION_JSON)
public class PlayerActionsService extends BaseResource {

	private static Logger log = LogManager.getLogger(PlayerActionsService.class);

	@POST
	@Path("/map-action")
	@Produces(MediaType.APPLICATION_JSON)
	public Response mapAction(@PathParam("id") String playerId, MoveMapElementAction a) {
		BaseResponse res = buildBaseResponse();
		try {
			if (playerId == null) {
				ResourceUtil.buildBadRequestResponse(res);
			}
			a.setPlayer(playerId);
			List<String> errors = PlayerActionsController.getInstance().executeAction(a);
			res.setBody(errors);
			if (errors.size() == 0) {
				return ResourceUtil.buildOkResponse(res);
			} else {
				res.setStatus(Status.BAD_REQUEST);
				res.setMessage("Errors occurred: see @body");
				return ResourceUtil.buildBadRequestResponse(res);
			}
		} catch (Exception e) {
			log.error(e, e);
			if (e instanceof GamesiteException) {
				res.setStatus(Status.fromStatusCode(500));
				// res.setErrorCode(((GamesiteException) e).getCode());
				res.setMessage(((GamesiteException) e).getMessage());
			} else {
				res.setMessage(e.getMessage());
			}
			return Response.status(res.getStatus()).entity(res).type(MediaType.APPLICATION_JSON).build();
		}

	}

	@POST
	@Path("/research-action")
	@Produces(MediaType.APPLICATION_JSON)
	public Response researchAction(@PathParam("id") String playerId, ResearchAction a) {
		BaseResponse res = buildBaseResponse();
		try {
			if (playerId == null) {
				ResourceUtil.buildBadRequestResponse(res);
			}
			a.setPlayer(playerId);
			List<String> errors = PlayerActionsController.getInstance().executeAction(a);
			res.setBody(errors);
			if (errors.size() == 0) {
				return ResourceUtil.buildOkResponse(res);
			} else {
				res.setStatus(Status.BAD_REQUEST);
				res.setMessage("Errors occurred: see @body");
				return ResourceUtil.buildBadRequestResponse(res);
			}
		} catch (Exception e) {
			log.error(e, e);
			if (e instanceof GamesiteException) {
				res.setStatus(Status.fromStatusCode(500));
				// res.setErrorCode(((GamesiteException) e).getCode());
				res.setMessage(((GamesiteException) e).getMessage());
			} else {
				res.setMessage(e.getMessage());
			}
			return Response.status(res.getStatus()).entity(res).type(MediaType.APPLICATION_JSON).build();
		}

	}

}
