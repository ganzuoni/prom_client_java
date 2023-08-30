package io.prometheus.client.exporter;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import io.prometheus.client.CollectorScrapingContextSupport;

/**
 * CollectorScrapingContext implementation for HTTPServer endpoint
 */
public class HttpCollectorScrapingContext extends CollectorScrapingContextSupport {

	protected HttpExchange httpExchange;
	private Map<String, List<String>> queryMap;

	public HttpCollectorScrapingContext(HttpExchange httpExchange) {
		super();
		String query = httpExchange.getRequestURI().getQuery();
		queryMap = parseQuery(query);
		this.httpExchange = httpExchange;
	}

	@Override
	public String getRequestURI() {
		URI requestURI = httpExchange.getRequestURI();
		String uri = requestURI.toString();
		int qx = uri.indexOf('?');
		if (qx != -1) {
			uri = uri.substring(0, qx);
		}
		return uri;
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = new String[0];
		List<String> vlist = queryMap.get(name);
		if (vlist != null) {
			values = vlist.toArray(values);
		}
		return values;
	}

	@Override
	public String getContextPath() {
		String contextPath = httpExchange.getHttpContext().getPath();
		return contextPath;
	}

	protected static Map<String, List<String>> parseQuery(String query) {
		Map<String, List<String>> queryMap = new HashMap<String, List<String>>();
		if (query != null) {
			String[] pairs = query.split("&");
			for (String pair : pairs) {
				String name = null;
				String val = null;
				int idx = pair.indexOf("=");
				if (idx != -1) {
					val = pair.substring(idx + 1);
					name = pair.substring(0, idx);
				} else {
					name = pair;
				}
				List<String> vals = queryMap.get(name);
				if (vals == null) {
					vals = new ArrayList<String>();
					queryMap.put(name, vals);
				}
				vals.add(val);
			}
		}
		return queryMap;
	}

}
