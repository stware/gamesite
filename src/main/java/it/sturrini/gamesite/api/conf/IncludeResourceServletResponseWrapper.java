package it.sturrini.gamesite.api.conf;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Estende HttpServletResponseWrapper, ed è da usare, ad esempio, nel caso in cui si voglia
 * includere una risorsa. Ad esempio:
 *
 * com.geko.web.IncludeResourceServletResponseWrapper wrapper = new com.geko.web.IncludeResourceServletResponseWrapper(response);
 * RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
 * rd.include(request, wrapper);
 *
 * In questo modo, chiamando wrapper.getBuffer(), si ha il contenuto della risorsa inclusa.
 *
 * @author Marco
 *
 */
public class IncludeResourceServletResponseWrapper extends javax.servlet.http.HttpServletResponseWrapper {

	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(IncludeResourceServletResponseWrapper.class);

	private StringWriter writer = new StringWriter();

	public IncludeResourceServletResponseWrapper(HttpServletResponse response) {
		super(response);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return super.getOutputStream();
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return new PrintWriter(writer);
	}

	public String getBuffer() {
		return writer.toString();
	}
}
