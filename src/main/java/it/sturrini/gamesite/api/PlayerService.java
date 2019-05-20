package it.sturrini.gamesite.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.sturrini.common.exception.GamesiteException;
import it.sturrini.gamesite.api.conf.BaseResource;
import it.sturrini.gamesite.api.conf.BaseResponse;
import it.sturrini.gamesite.api.conf.ResourceUtil;
import it.sturrini.gamesite.controllers.MapController;
import it.sturrini.gamesite.controllers.player.PlayerController;
import it.sturrini.gamesite.dao.SearchFilter;
import it.sturrini.gamesite.model.Player;
import it.sturrini.gamesite.model.map.Map;

@Path("/player")
@Consumes(MediaType.APPLICATION_JSON)
public class PlayerService extends BaseResource {

	private static Logger log = LogManager.getLogger(PlayerService.class);

	@PUT
	@Path("/")
	public Response signup(Player player) {
		BaseResponse res = buildBaseResponse();
		try {
			if (player == null) {
				ResourceUtil.buildBadRequestResponse(res);
			}
			Player signup = PlayerController.getInstance().signup(player);
			res.setBody(signup);
			return ResourceUtil.buildOkResponse(res);
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
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(Player player) {
		BaseResponse res = buildBaseResponse();
		try {
			if (player == null) {
				ResourceUtil.buildBadRequestResponse(res);
			}
			String login = PlayerController.getInstance().login(player);

			res.setBody(login);
			return ResourceUtil.buildOkResponse(res);
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
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePlayer(@PathParam("id") String playerId, Player player) {
		BaseResponse res = buildBaseResponse();
		try {
			if (player == null) {
				ResourceUtil.buildBadRequestResponse(res);
			}
			PlayerController.getInstance().update(playerId, player);
			return ResourceUtil.buildOkResponse(res);
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
	@Path("/{id}/maps/{mapId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCity(@PathParam("id") String playerId, @PathParam("mapId") String mapId, Map map) {
		BaseResponse res = buildBaseResponse();
		try {
			if (playerId == null || mapId == null) {
				ResourceUtil.buildBadRequestResponse(res);
			}
			Map saved = MapController.getInstance().updateMap(playerId, mapId, map);
			res.setBody(saved);
			return ResourceUtil.buildOkResponse(res);
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

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPlayer(@PathParam("id") String playerId) {

		BaseResponse<Player> res = buildBaseResponse();
		try {
			if (playerId == null) {
				return ResourceUtil.buildNotFoundResponse(res);
			}
			Player player = PlayerController.getInstance().getPlayer(playerId);
			if (player != null) {
				res.setBody(player);
			} else {
				return ResourceUtil.buildNotFoundResponse(res);
			}
			return ResourceUtil.buildOkResponse(res);
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

	@GET
	@Path("/find")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPlayers(@QueryParam("id") String playerId, @QueryParam("limit") Long limit) {

		BaseResponse<List<Player>> res = buildBaseResponse();
		try {
			if (playerId != null || limit != null) {
				res.setMessage("Not yet implemented");
				return ResourceUtil.buildBadRequestResponse(res);
			}
			List<Player> players = PlayerController.getInstance().findPlayers(new SearchFilter());
			res.setBody(players);
			return ResourceUtil.buildOkResponse(res);
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
