package io.prometheus.client.servlet.common.exporter;

import io.prometheus.client.CollectorScrapingContextSupport;
import io.prometheus.client.servlet.common.adapter.HttpServletRequestAdapter;

/**
 * CollectorScrapingContext implementation for Servlet endpoint
 */

public class ServletCollectorScrapingContext extends CollectorScrapingContextSupport {

	protected HttpServletRequestAdapter request;

	public ServletCollectorScrapingContext(HttpServletRequestAdapter request) {
		super();
		this.request = request;
	}

	public String getRequestURI() {
		String uri = request.getRequestURI();
		int qx = uri.indexOf('?');
		if (qx != -1) {
			uri = uri.substring(0, qx);
		}
		return uri;
	}

	public String[] getParameterValues(String name) {
		return request.getParameterValues(name);
	}

	public String getContextPath() {
		return request.getContextPath();
	}
	
	
	
}
