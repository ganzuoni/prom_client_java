package io.prometheus.client;

public interface CollectorScrapingContext {
    String getRequestURI();
    String getContextPath();
    String[] getParameterValues(String name);
    Predicate<String> getSampleNameFilter();
}
