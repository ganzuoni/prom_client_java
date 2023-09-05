package io.prometheus.client.exporter;

import static org.assertj.core.api.Java6Assertions.assertThat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sun.net.httpserver.HttpServer;

import io.prometheus.client.Collector;
import io.prometheus.client.Collector.MetricFamilySamples.Sample;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.CollectorScrapingContext;

public class TestHTTPServerScrapingContext {

	CollectorRegistry registry;

	HttpRequest.Builder createHttpRequestBuilder(HTTPServer httpServer, String urlPath) {
		return new HttpRequest.Builder().withURL("http://localhost:" + httpServer.getPort() + urlPath);
	}

	HttpRequest.Builder createHttpRequestBuilder(HttpServer httpServer, String urlPath) {
		return new HttpRequest.Builder().withURL("http://localhost:" + httpServer.getAddress().getPort() + urlPath);
	}

	@Before
	public void init() throws IOException {
		registry = new CollectorRegistry();
		new ExtendedCollector().register(registry);
	}

	@Test
	public void testSimpleRequest() throws IOException {
		HTTPServer httpServer = new HTTPServer(new InetSocketAddress(0), registry, false, true);

		try {
			String body = createHttpRequestBuilder(httpServer, "/metrics").build().execute().getBody();
			assertThat(body).contains("defaultTarget");
		} finally {
			httpServer.close();
		}
	}

	@Test
	public void testSingleName() throws IOException {
		HTTPServer httpServer = new HTTPServer(new InetSocketAddress(0), registry, false, true);

		try {
			String body = createHttpRequestBuilder(httpServer, "/metrics?target=promTarget").build().execute().getBody();
			assertThat(body).contains("promTarget");
		} finally {
			httpServer.close();
		}
	}

	class ExtendedCollector extends Collector {

		@Override
		public List<MetricFamilySamples> collect() {
			return new ArrayList<Collector.MetricFamilySamples>();
		}

		@Override
		public boolean supportsCollectorScrapingContext() {
			return true;
		}

		@Override
		protected List<MetricFamilySamples> collectSamples(CollectorScrapingContext scrapingContext) {
			String[] targetName = scrapingContext.getParameterValues("target");
			List<String> labelsList = new ArrayList<String>();
			labelsList.add("target");
			List<String> labelValuesList = new ArrayList<String>();
			Sample sample = null;
			if (targetName == null || targetName.length == 0) {
				labelValuesList.add("defaultTarget");
				sample = new MetricFamilySamples.Sample("a_total", labelsList, labelValuesList, 1.0);
			} else {
				labelValuesList.add(targetName[0]);
				sample = new MetricFamilySamples.Sample("a_total", labelsList, labelValuesList, Math.random());
			}
			return Arrays.<MetricFamilySamples> asList(new MetricFamilySamples("a_total", Type.COUNTER, "help", Arrays.asList(sample)));
		}
	}

}
