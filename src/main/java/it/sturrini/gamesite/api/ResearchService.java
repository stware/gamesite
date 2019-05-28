package it.sturrini.gamesite.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import it.sturrini.gamesite.controllers.player.PlayerController;
import it.sturrini.gamesite.controllers.researches.ResearchController;
import it.sturrini.gamesite.model.Player;
import it.sturrini.gamesite.model.research.Research;

@Path("/research")
@Consumes(MediaType.APPLICATION_JSON)
public class ResearchService extends BaseResource {

	private static Logger log = LogManager.getLogger(ResearchService.class);

	@GET
	@Path("/{playerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getResearchList(@PathParam("playerId") String playerId) {

		BaseResponse<List<Research>> res = buildBaseResponse();
		try {
			if (playerId == null) {
				return ResourceUtil.buildNotFoundResponse(res);
			}
			Player player = PlayerController.getInstance().getPlayer(playerId);
			if (player == null) {
				res.setMessage("Player not found");
				return ResourceUtil.buildNotFoundResponse(res);
			}
			List<Research> researches = ResearchController.getInstance().getResearches(player);
			res.setBody(researches);
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
