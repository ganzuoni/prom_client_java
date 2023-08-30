package io.prometheus.client;

/**
 * Infos extracted from the request received by the endpoint
 * 
 */
public interface CollectorScrapingContext {
	String getRequestURI();

	String getContextPath();

	String[] getParameterValues(String name);

	Predicate<String> getSampleNameFilter();
}
