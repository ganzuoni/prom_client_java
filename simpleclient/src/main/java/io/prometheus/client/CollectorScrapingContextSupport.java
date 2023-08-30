package io.prometheus.client;

public abstract class CollectorScrapingContextSupport implements CollectorScrapingContext {

	protected Predicate<String> sampleNameFilter;

	public Predicate<String> getSampleNameFilter() {
		return sampleNameFilter;
	}

	public void setSampleNameFilter(Predicate<String> sampleNameFilter) {
		this.sampleNameFilter = sampleNameFilter;
	}
	
}
