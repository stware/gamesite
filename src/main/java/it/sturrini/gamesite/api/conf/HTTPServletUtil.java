package it.sturrini.gamesite.api.conf;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Classe di utility per ispezionare la HttpServletRequest e la HttpSession.
 *
 * @author Marco
 */
public class HTTPServletUtil {

	private static final Pattern INVALID_FILENAME_CHARS = Pattern.compile("[\\*<>\\[\\]=\\+\"\\\\\\/,\\:\\;]");

	private static Log log = LogFactory.getLog(HTTPServletUtil.class);

	/**
	 * Effettua il forward (server-side) all'url indicato da page.
	 *
	 * @param req
	 * @param res
	 * @param page
	 */
	public static void forward(HttpServletRequest req, ServletResponse res, String page) {
		try {
			RequestDispatcher dis = req.getRequestDispatcher(page);
			dis.forward(req, res);
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	/**
	 * Ellettua il redirect (client-side) all'url indicato da page.
	 *
	 * @param res
	 * @param page
	 */
	public static void redirect(HttpServletResponse res, String page) {
		try {
			res.sendRedirect(page);
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	/**
	 * Ritorna il contenuto della risorsa indicata da url.
	 *
	 * @param url
	 * @param request
	 * @param response
	 * @return
	 */
	public static String includeResource(String url, HttpServletRequest request, HttpServletResponse response) {
		IncludeResourceServletResponseWrapper wrapper = new IncludeResourceServletResponseWrapper(response);
		RequestDispatcher rd = request.getRequestDispatcher(url);
		try {
			rd.include(request, wrapper);
		} catch (Exception e) {
			log.error("Unable to include: " + url, e);
		}
		return wrapper.getBuffer();
	}

	/**
	 * Parametro caricato da web.xml.
	 *
	 * @param application
	 * @param paramName
	 * @param defaultValue
	 * @return
	 */
	public static String getInitParameterAsString(ServletContext application, String paramName, String defaultValue) {
		String value = defaultValue;
		String param = application.getInitParameter(paramName);
		if (param != null) {
			value = param;
		}
		return value;
	}

	/**
	 * Parametro caricato da web.xml.
	 *
	 * @param application
	 * @param paramName
	 * @param defaultValue
	 * @return
	 */
	public static boolean getInitParameterAsBoolean(ServletContext application, String paramName, boolean defaultValue) {
		boolean value = defaultValue;
		String param = application.getInitParameter(paramName);
		if (param != null) {
			value = Boolean.valueOf(param);
		}
		return value;
	}

	/**
	 * Scrive il contenuto del buffer sulla response.
	 *
	 * @param buffer
	 *        il buffer da scrivere
	 * @param response
	 *        la response su cui scrivere
	 * @param fileName
	 *        il nome da dare al file
	 * @throws Exception
	 */
	public static void sendBuffer(byte[] buffer, HttpServletResponse response, String fileName) throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(buffer);
		OutputStream out = null;
		String mimeType = MimeTypeUtil.getMimeType(in);
		response.setContentType(mimeType);
		// sfrutto il fatto che il ByteArrayInputStream al reset manda il
		// puntatore all' inizio
		in.reset();
		try {
			in = new ByteArrayInputStream(buffer);

			response.setHeader("Content-encoding", "application/octet-stream");
			response.setHeader("Content-disposition", "attachment; filename=\"" + cleanFileName(fileName) + "\"");
			response.setContentLength(buffer.length);
			out = response.getOutputStream();

			IOUtils.copy(in, out);
			out.flush();

		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
			}
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Ripulisce il nome di un file da caratteri non permessi in windows come ad
	 * esempio *<>[]=+"\/,.:;
	 *
	 * @param name
	 * @return
	 */
	public static String cleanFileName(String name) {
		if (name == null) {
			return null;
		} else {
			return INVALID_FILENAME_CHARS.matcher(name).replaceAll("_");
		}
	}

	/**
	 * Ritorna l'host (ie: "www.gecod.com") leggendolo dall'header Host.
	 *
	 * @param request
	 * @return
	 */
	public static String getHost(HttpServletRequest request) {
		return getHost(request, null);
	}

	/**
	 * Ritorna l'host con/senza il protocollo (ie: "http://www.gecod.com")
	 * leggendolo dall'header Host.
	 *
	 * @param protocol
	 *        il protocollo da usare come prefisso dell'host (ad es.
	 *        http://)
	 * @param request
	 * @return
	 */
	public static String getHost(HttpServletRequest request, String protocol) {
		StringBuilder host = new StringBuilder();
		if (protocol != null) {
			host.append(protocol);
		}
		try {
			host.append(request.getHeader("host"));
		} catch (Exception e) {
			log.error("Errore nel recuperare l'host dell'header", e);
			host = new StringBuilder();
		}

		return host.toString();
	}

	/**
	 * Usato dai task che sono chiamati in get (e post). In funzione del metodo
	 * e dell'encoding (get: iso-8859-1; post: utf-8) legge il parametro http
	 * correttamente.
	 *
	 * @param request
	 * @param parameterName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getParameter(HttpServletRequest request, String parameterName) {
		String res = null;
		String parameter = request.getParameter(parameterName);
		String method = request.getMethod();

		if (parameter != null) {
			if (method.equalsIgnoreCase("GET")) {
				try {
					res = new String(parameter.getBytes("iso-8859-1"), "utf-8");
				} catch (UnsupportedEncodingException e) {
					log.error(e, e);
				}
			} else if (method.equalsIgnoreCase("POST")) {
				res = parameter;
			}
		}

		return (res);
	}

	/**
	 * Usato dai task che sono chiamati in get (e post). In funzione del metodo
	 * e dell'encoding (get: iso-8859-1; post: utf-8) legge i parametri http
	 * correttamente.
	 *
	 * @param request
	 * @param parameterName
	 * @return
	 */
	public static String[] getParameters(HttpServletRequest request, String parameterName) {
		String[] res = null;

		String method = request.getMethod();
		String[] parameters = request.getParameterValues(parameterName);

		if (parameters != null && parameters.length > 0) {
			res = new String[parameters.length];

			for (int i = 0; i < parameters.length; i++) {
				if (method.equalsIgnoreCase("GET")) {
					try {
						res[i] = new String(parameters[i].getBytes("iso-8859-1"), "utf-8");
					} catch (UnsupportedEncodingException e) {
						log.error(e, e);
					}
				} else if (method.equalsIgnoreCase("POST")) {
					res[i] = parameters[i];
				}
			}
		}

		return res;
	}

	/**
	 * Ritorna una mappa contenente tutti i parametri HTTP della request. La
	 * chiave è il nome del parametro.
	 *
	 * @param request
	 * @return
	 */
	public static Map<String, String[]> getParameters(HttpServletRequest request) {
		return request.getParameterMap();
	}

	/**
	 * Ritorna una mappa contenente tutti gli attributi della request e della
	 * sessione. La chiave può essere "request" o "session". Il valore è una
	 * HashMap contenente gli effettivi attributi.
	 *
	 * @param request
	 * @return
	 */
	public static Map<String, Map<String, Object>> getAttributes(HttpServletRequest request) {
		Map<String, Map<String, Object>> res = new HashMap<String, Map<String, Object>>();
		res.put("request", getRequestAttributes(request));
		res.put("session", getSessionAttributes(request.getSession()));

		return res;
	}

	/**
	 * Ritorna gli attributi della request.
	 *
	 * @param request
	 * @return
	 */
	public static Map<String, Object> getRequestAttributes(HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();

		Enumeration<String> names = request.getAttributeNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();

			Object o = request.getAttribute(name);
			res.put(name, o);
		}

		return res;
	}

	/**
	 * Ritorna gli attributi della session.
	 *
	 * @param session
	 * @return
	 */
	public static Map<String, Object> getSessionAttributes(HttpSession session) {
		Map<String, Object> res = new HashMap<String, Object>();

		Enumeration<String> names = session.getAttributeNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();

			Object o = session.getAttribute(name);
			res.put(name, o);
		}

		return res;
	}

	/**
	 * Stampa nel logger i parametri http della request.
	 *
	 * @param req
	 */
	public static void showParameters(HttpServletRequest req) {
		log.debug(printParameters(req));
	}

	/**
	 * Stampa i parametri http della request.
	 *
	 * @param req
	 * @return
	 */
	public static String printParameters(HttpServletRequest req) {
		StringBuffer res = new StringBuffer("HTTP Parameters: \n");
		Enumeration<String> names = req.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String[] values = req.getParameterValues(name);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < values.length; i++) {
				if (i != 0) {
					sb.append(" # ");
				}
				sb.append(values[i]);
			}
			res.append(name + ": " + sb.toString() + "\n");
		}

		return res.toString();
	}

	/**
	 * Stampa nel logger gli attributi presenti in request.
	 *
	 * @param req
	 */
	public static void showRequestAttributes(HttpServletRequest req) {
		log.debug(printRequestAttributes(req));
	}

	/**
	 * Stampa gli attributi presenti in request.
	 *
	 * @param req
	 * @return
	 */
	public static String printRequestAttributes(HttpServletRequest req) {
		StringBuffer res = new StringBuffer("Request Attributes: \n");
		Object obj = null;
		Enumeration<String> names = req.getAttributeNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();

			obj = req.getAttribute(name);
			res.append(name + ": " + obj.toString() + "\n");
		}
		return res.toString();
	}

	/**
	 * Stampa gli attributi presenti nel servlet context.
	 *
	 * @param sc
	 * @return
	 */
	public static String printServletContextAttributes(ServletContext sc) {
		StringBuffer res = new StringBuffer("Servlet Context Attributes: \n");
		Object obj = null;
		Enumeration<String> names = sc.getAttributeNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();

			obj = sc.getAttribute(name);
			res.append(name + ": " + obj.toString() + "\n");
		}
		return res.toString();
	}

	/**
	 * Stampa nel logger gli attributi presenti in sessione.
	 *
	 * @param req
	 */
	public static void showSessionAttributes(HttpServletRequest req) {
		log.debug(printSessionAttributes(req));

	}

	/**
	 * Stampa gli attributi presenti in sessione.
	 *
	 * @param req
	 * @return
	 */
	public static String printSessionAttributes(HttpServletRequest req) {
		StringBuffer res = new StringBuffer("Session Attributes: \n");
		Object obj = null;
		Enumeration<String> names = req.getSession().getAttributeNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();

			obj = req.getSession().getAttribute(name);
			res.append(name + ": " + obj.toString() + "\n");
		}
		return res.toString();
	}

	/**
	 * Stampa nel logger gli header della richiesta http.
	 *
	 * @param req
	 */
	public static void showHeaders(HttpServletRequest req) {
		log.debug(printHeaders(req));
	}

	/**
	 * Stampa gli header della richiesta http.
	 *
	 * @param req
	 * @return
	 */
	public static String printHeaders(HttpServletRequest req) {
		StringBuffer res = new StringBuffer("HTTP Headers: ");

		Enumeration<String> names = req.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = req.getHeader(name);
			res.append(name + ": " + value + "\n");
		}
		return res.toString();
	}

	/**
	 * Stampa nel logger i cookie contenuti nella request.
	 *
	 * @param req
	 */
	public static void showCookies(HttpServletRequest req) {
		log.debug(printCookies(req));
	}

	/**
	 * Stampa i cookie contenuti nella request.
	 *
	 * @param req
	 * @return
	 */
	public static String printCookies(HttpServletRequest req) {
		StringBuffer res = new StringBuffer("HTTP Cookies: \n");

		Cookie[] c = req.getCookies();
		if (c != null) {
			for (int i = 0; i < c.length; i++) {
				res.append("Name: " + c[i].getName() + "\n");
				res.append("Value: " + c[i].getValue() + "\n");
				res.append("Domain: " + c[i].getDomain() + "\n");
				res.append("Path: " + c[i].getPath() + "\n");
			}
		}
		return res.toString();
	}

	/**
	 * Stampa nel logger tutte le informazioni relative alla HttpSession.
	 *
	 * @param req
	 */
	public static void debugHTTPSession(HttpServletRequest req) {
		log.debug(printHTTPSession(req));
	}

	/**
	 * Stampa tutte le informazioni relative alla HttpSession.
	 *
	 * @param req
	 * @return
	 */
	public static String printHTTPSession(HttpServletRequest req) {
		StringBuffer res = new StringBuffer("Session info: ");

		HttpSession session = req.getSession();

		res.append("Id: " + session.getId() + "\n");
		res.append("New: " + session.isNew() + "\n");
		res.append("Timeout (sec): " + session.getMaxInactiveInterval() + "\n");
		res.append("Creation time: " + new Date(session.getCreationTime()) + "\n");
		res.append("Last accessed time: " + new Date(session.getLastAccessedTime()) + "\n");

		res.append("Requested session id from cookie: " + req.isRequestedSessionIdFromCookie() + "\n");
		res.append("Requested session id from URL: " + req.isRequestedSessionIdFromURL() + "\n");
		res.append("Requested session id is valid: " + req.isRequestedSessionIdValid() + "\n");

		res.append(printSessionAttributes(req));

		return res.toString();
	}

	/**
	 * Stampa nel logger tutte le informazioni relative alla HttpServletRequest.
	 *
	 * @param req
	 */
	public static void debugHTTPRequest(HttpServletRequest req) {
		log.debug(printHTTPRequest(req));
	}

	/**
	 * Stampa tutte le informazioni relative alla HttpServletRequest.
	 *
	 * @param req
	 * @return
	 */
	public static String printHTTPRequest(HttpServletRequest req) {
		StringBuffer res = new StringBuffer();

		res.append("getAuthType: " + req.getAuthType() + "\n");
		res.append("getCharacterEncoding: " + req.getCharacterEncoding() + "\n");
		res.append("getContentLength: " + req.getContentLength() + "\n");
		res.append("getContentType: " + req.getContentType() + "\n");
		res.append("getContextPath: " + req.getContextPath() + "\n");
		res.append("getLocalAddr: " + req.getLocalAddr() + "\n");
		res.append("getLocale: " + req.getLocale() + "\n");
		res.append("getLocalName: " + req.getLocalName() + "\n");
		res.append("getLocalPort: " + req.getLocalPort() + "\n");
		res.append("getMethod: " + req.getMethod() + "\n");
		res.append("getPathInfo: " + req.getPathInfo() + "\n");
		res.append("getPathTranslated: " + req.getPathTranslated() + "\n");
		res.append("getProtocol: " + req.getProtocol() + "\n");
		res.append("getQueryString: " + req.getQueryString() + "\n");
		res.append("getRemoteAddr: " + req.getRemoteAddr() + "\n");
		res.append("getRemoteHost: " + req.getRemoteHost() + "\n");
		res.append("getRemotePort: " + req.getRemotePort() + "\n");
		res.append("getRemoteUser: " + req.getRemoteUser() + "\n");
		res.append("getRequestedSessionId: " + req.getRequestedSessionId() + "\n");
		res.append("getRequestURI: " + req.getRequestURI() + "\n");
		res.append("getRequestURL: " + req.getRequestURL() + "\n");
		res.append("getScheme: " + req.getScheme() + "\n");
		res.append("getServerName: " + req.getServerName() + "\n");
		res.append("getServerPort: " + req.getServerPort() + "\n");
		res.append("getServletPath: " + req.getServletPath() + "\n");
		res.append("getUserPrincipal: " + (req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : null) + "\n");

		res.append(printHeaders(req));
		res.append(printCookies(req));
		res.append(printParameters(req));
		res.append(printRequestAttributes(req));

		return res.toString();
	}

}