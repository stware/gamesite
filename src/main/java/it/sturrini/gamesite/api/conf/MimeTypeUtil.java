package it.sturrini.gamesite.api.conf;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil2;
import eu.medsea.mimeutil.detector.MimeDetector;

/**
 * Legge il file indicato e ritorna il mimetype associato. Questa associazione è definita in
 * opportuni file di configurazione, in funzione del {@link MimeDetector} utilizzato, contenuti
 * nella libreria mime-util.jar.
 * 
 * @author Marco
 */
@SuppressWarnings("unchecked")
public class MimeTypeUtil {

	private static MimeUtil2 mu = new MimeUtil2();

	static {
		// mu.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
		mu.registerMimeDetector("com.geko.util.MimeTypeCustomDetector");
	}

	/**
	 * Ritorna il mimetype del file indicato in is.
	 * 
	 * @param is
	 * @return
	 */
	public static String getMimeType(InputStream is) {
		return (getMimeType(is, null, null, true, false));
	}

	/**
	 * Ritorna la descrizione del mimetype del file indicato in is.
	 * 
	 * @param is
	 * @return
	 */
	public static String getMimeTypeDescription(InputStream is) {
		return (getMimeType(is, null, null, false, true));
	}

	/**
	 * Ritorna il mimetype del file indicato in f.
	 * 
	 * @param f
	 * @return
	 */
	public static String getMimeType(File f) {
		return (getMimeType(null, f, null, true, false));
	}

	/**
	 * Ritorna la descrizione del mimetype del file indicato in f.
	 * 
	 * @param f
	 * @return
	 */
	public static String getMimeTypeDescription(File f) {
		return (getMimeType(null, f, null, false, true));
	}

	/**
	 * Ritorna il mimetype del file indicato in url.
	 * 
	 * @param url
	 * @return
	 */
	public static String getMimeType(URL url) {
		return (getMimeType(null, null, url, true, false));
	}

	/**
	 * Ritorna la descrizione del mimetype del file indicato in url.
	 * 
	 * @param url
	 * @return
	 */
	public static String getMimeTypeDescription(URL url) {
		return (getMimeType(null, null, url, false, true));
	}

	private static String getMimeType(InputStream is, File f, URL url, boolean displayName, boolean displayDescription) {
		Collection<MimeType> mt = null;
		if (is != null) {
			mt = mu.getMimeTypes(is);
		} else if (f != null) {
			if (f.isDirectory()) {
				mt = new HashSet<MimeType>();
				mt.add(MimeUtil2.DIRECTORY_MIME_TYPE);
			} else {
				mt = mu.getMimeTypes(f);
			}
		} else if (url != null) {
			mt = mu.getMimeTypes(url);
		}

		MimeType mime = null;
		if (mt != null && !mt.isEmpty()) {
			mime = MimeUtil2.getMostSpecificMimeType(mt);
		}

		if (displayName || displayDescription) {
			return (mime != null ? mime.toString() : null);
		}

		return MimeUtil2.UNKNOWN_MIME_TYPE.toString();
	}

}