package it.sturrini.gamesite.api.conf;

import it.sturrini.common.StringUtil;

public final class HostInfo {

	private String host;
	private String scheme;
	private int port;

	public static HostInfo fromUrl(final String url) {
		HostInfo res = null;

		if (!StringUtil.isEmpty(url)) {
			res = new HostInfo();

			final int startPos = url.indexOf("://") + 3;
			if (startPos == 2) {
				// we consider this illegal
				return null;
			}
			res.scheme = url.substring(0, startPos - 3);

			final int paramStart = url.indexOf('?');
			final String hostAndPath = (paramStart == -1 ? url : url.substring(0, paramStart));
			final int endPos = hostAndPath.indexOf('/', startPos);
			final String hostPart = (endPos == -1 ? hostAndPath.substring(startPos) : hostAndPath.substring(startPos, endPos));
			final int hostNameStart = hostPart.indexOf('@') + 1;
			final int hostNameEnd = hostPart.lastIndexOf(':');
			if (hostNameEnd < hostNameStart) {
				res.host = hostPart.substring(hostNameStart);
				if (res.scheme.equals("http")) {
					res.port = 80;
				} else if (res.scheme.equals("https")) {
					res.port = 443;
				}
			} else {
				res.host = hostPart.substring(hostNameStart, hostNameEnd);
				res.port = Integer.valueOf(hostPart.substring(hostNameEnd + 1));
			}
		}

		return res;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the scheme
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

}