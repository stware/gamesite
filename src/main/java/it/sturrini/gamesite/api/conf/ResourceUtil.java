package it.sturrini.gamesite.api.conf;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResourceUtil {

	private static Logger log = LogManager.getLogger(ResourceUtil.class);

	public static Response buildOkResponse() {
		return Response.status(Status.OK).entity(new BaseResponse<>(Status.OK, "OK")).type(MediaType.APPLICATION_JSON).build();
	}

	public static Response buildOkResponse(BaseResponse<?> entity) {
		return Response.status(Status.OK).entity(entity).type(MediaType.APPLICATION_JSON).build();
	}

	public static Response buildOkResponse(BaseResponse<?> entity, Cookie cookie) {
		return Response.status(Status.OK).cookie(new NewCookie(cookie)).entity(entity).type(MediaType.APPLICATION_JSON).build();
	}

	public static Response buildStreamingOutputResponse(String contentType, String fileName, StreamingOutput so) {
		return buildStreamingOutputResponse(contentType, fileName, true, so);
	}

	public static Response buildStreamingOutputResponse(String contentType, String fileName, boolean addContentDispositionHeader, StreamingOutput so) {
		ResponseBuilder rb = Response.status(Status.OK).entity(so).header(HttpHeaders.CONTENT_TYPE, contentType);
		if (addContentDispositionHeader) {
			rb = rb.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
		}
		return rb.build();
	}

	public static Response buildBadRequestResponse() {
		return Response.status(Status.BAD_REQUEST).entity(new BaseResponse<>(Status.BAD_REQUEST, "Bad Request")).type(MediaType.APPLICATION_JSON).build();
	}

	public static Response buildBadRequestResponse(BaseResponse<?> entity) {
		return Response.status(Status.BAD_REQUEST).entity(entity).type(MediaType.APPLICATION_JSON).build();
	}

	public static Response buildNotFoundResponse() {
		return Response.status(Status.NOT_FOUND).entity(new BaseResponse<>(Status.NOT_FOUND, "Not Found")).type(MediaType.APPLICATION_JSON).build();
	}

	public static Response buildNotFoundResponse(BaseResponse<?> entity) {
		return Response.status(Status.NOT_FOUND).entity(entity).type(MediaType.APPLICATION_JSON).build();
	}

	public static Response buildNoContentResponse() {
		return Response.noContent().type(MediaType.APPLICATION_JSON).build();
	}

	public static Response buildConflictResponse() {
		return Response.status(Status.CONFLICT).type(MediaType.APPLICATION_JSON).build();
	}

	public static Response buildUnauthorizedResponse() {
		return Response.status(Status.UNAUTHORIZED).entity(new BaseResponse<>(Status.UNAUTHORIZED, "Unauthorized")).type(MediaType.APPLICATION_JSON).build();
	}

	public static Response buildUnauthorizedResponse(BaseResponse<?> entity) {
		return Response.status(Status.UNAUTHORIZED).entity(entity).type(MediaType.APPLICATION_JSON).build();
	}

	public static Response buildForbiddenResponse(BaseResponse<?> entity) {
		return Response.status(Status.FORBIDDEN).entity(entity).type(MediaType.APPLICATION_JSON).build();
	}

	public static Response buildForbiddenResponse() {
		return Response.status(Status.FORBIDDEN).entity(new BaseResponse<>(Status.FORBIDDEN, "Forbidden")).type(MediaType.APPLICATION_JSON).build();
	}

	public static Response buildErrorResponse() {
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new BaseResponse<>(Status.INTERNAL_SERVER_ERROR, "Internal Server Error")).type(
				MediaType.APPLICATION_JSON).build();
	}

	public static Response buildErrorResponse(BaseResponse<?> entity) {
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(entity).type(MediaType.APPLICATION_JSON).build();
	}

	public static Response buildErrorResponse(Throwable t) {
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new BaseResponse<>(Status.INTERNAL_SERVER_ERROR, t.getClass().getName() + ": " + t.getMessage())).type(
				MediaType.APPLICATION_JSON).build();
	}

	public static <T> T jsonToBean(String json, Class<T> clazz) {
		T res = null;

		if (json != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				res = mapper.readValue(json, clazz);
			} catch (Exception e) {
				log.warn("Unable to parse json for class " + clazz, e);
			}
		}

		return res;
	}

	public static <T> T jsonToBean(String json) {
		T res = null;

		if (json != null) {
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<Map<String, Object>> type = new TypeReference<Map<String, Object>>() {

			};

			try {
				res = mapper.readValue(json, type);
			} catch (Exception e) {
				log.warn("Unable to parse json for type " + type.getType(), e);
			}
		}

		return res;
	}

	public static <T> T jsonToListOfBeans(String json) {
		T res = null;

		if (json != null) {
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Object>> type = new TypeReference<List<Object>>() {
			};

			try {
				res = mapper.readValue(json, type);
			} catch (Exception e) {
				log.warn("Unable to parse json for type " + type.getType(), e);
			}
		}

		return res;
	}

	public static <T> T jsonToBean(InputStream src, Class<T> clazz) {
		T res = null;

		ObjectMapper mapper = new ObjectMapper();
		try {
			res = mapper.readValue(src, clazz);
		} catch (Exception e) {
			log.warn("Unable to parse json for class " + clazz, e);
		}

		return res;
	}

	public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
		T res = null;

		if (map != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				res = mapper.convertValue(map, clazz);
			} catch (Exception e) {
				log.warn("Unable to convert map to instance of class " + clazz, e);
			}
		}

		return res;
	}

	public static Map<String, Object> beanToMap(Object bean) {
		if (bean != null) {
			TypeReference<Map<String, Object>> type = new TypeReference<Map<String, Object>>() {
			};

			return new ObjectMapper().convertValue(bean, type);
		}

		return null;
	}

	public static String beanToJson(Object bean) {
		String res = null;

		if (bean != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				res = mapper.writeValueAsString(bean);
			} catch (Exception e) {
				log.warn("Unable to serialize json for bean " + bean, e);
			}
		}

		return res;
	}

	public static void beanToJson(Object bean, OutputStream out) {
		if (bean != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				mapper.writeValue(out, bean);
			} catch (Exception e) {
				log.warn("Unable to serialize json for bean " + bean, e);
			}
		}
	}

}
