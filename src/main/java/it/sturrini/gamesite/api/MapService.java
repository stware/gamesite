package it.sturrini.gamesite.api;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import it.sturrini.gamesite.controllers.MapElementController;
import it.sturrini.gamesite.dao.SearchFilter;
import it.sturrini.gamesite.model.map.Map;
import it.sturrini.gamesite.model.map.MapElement;
import it.sturrini.gamesite.model.map.MapTypes;

@Path("/map")
@Consumes(MediaType.APPLICATION_JSON)
public class MapService extends BaseResource {

	private static Logger log = LogManager.getLogger(MapService.class);

	@GET
	@Path("/{playerId}/{type}")
	public Response getMapByPlayerAndType(@PathParam("playerId") String playerId, @PathParam("type") String type) {
		BaseResponse res = buildBaseResponse();
		try {
			if (playerId == null || type == null) {
				res.setMessage("Wrong player ID or Map Type");
				ResourceUtil.buildBadRequestResponse(res);
			}
			SearchFilter sf = new SearchFilter();
			sf.addParam("playerId", playerId);
			sf.addParam("mapType", MapTypes.valueOf(type).name());
			List<Map> find = MapController.getInstance().find(sf);
			res.setBody(find);
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
	public Response getMapById(@PathParam("id") String id) {
		BaseResponse res = buildBaseResponse();
		try {
			if (id == null) {
				res.setMessage("Wrong Map ID");
				ResourceUtil.buildBadRequestResponse(res);
			}
			Map find = MapController.getInstance().getById(id);
			res.setBody(find);
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
	@Path("/{playerId}/{type}/elements")
	public Response getMapElements(@PathParam("playerId") String playerId, @PathParam("type") String type) {
		BaseResponse res = buildBaseResponse();
		try {
			if (playerId == null || type == null) {
				res.setMessage("Wrong player ID or Map Type");
				ResourceUtil.buildBadRequestResponse(res);
			}
			SearchFilter sf = new SearchFilter();
			sf.addParam("playerId", playerId);
			sf.addParam("mapType", MapTypes.valueOf(type).name());
			List<Map> find = MapController.getInstance().find(sf);
			List<MapElement> out = new ArrayList<>();
			for (Map map : find) {
				String mapId = map.getId();
				SearchFilter sf2 = new SearchFilter();
				sf2.addParam("mapId", mapId);
				List<MapElement> result = MapElementController.getInstance().find(sf2);
				out.addAll(result);
			}
			res.setBody(out);
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
