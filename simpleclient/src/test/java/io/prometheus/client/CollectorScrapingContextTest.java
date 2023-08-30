package io.prometheus.client;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.Collector.MetricFamilySamples.Sample;

public class CollectorScrapingContextTest {


	public class MockCollectorScrapingContext implements CollectorScrapingContext {
		public Map<String, String[]> parametersValue = new HashMap<String, String[]>();

		@Override
		public String getRequestURI() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getContextPath() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String[] getParameterValues(String name) {
			// TODO Auto-generated method stub
			return parametersValue.get(name);
		}

		public void setParameterValues(String name, String[] values) {
			parametersValue.put(name, values);
		}


		@Override
		public Predicate<String> getSampleNameFilter() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	@Test
	public void testNoTarget() throws Exception {
		CollectorRegistry registry = new CollectorRegistry();
		new ExtendedCollector().register(registry);
		MockCollectorScrapingContext scrapingContext = new MockCollectorScrapingContext();
		runTest(registry, scrapingContext, "defaultTarget");

	}

	@Test
	public void testWithTarget() throws Exception {
		CollectorRegistry registry = new CollectorRegistry();
		new ExtendedCollector().register(registry);
		MockCollectorScrapingContext scrapingContext = new MockCollectorScrapingContext();
		scrapingContext.setParameterValues("target", new String[] {"target1000]"});
		runTest(registry, scrapingContext, "target1000]");

	}

	@Test
	public void testNoContext() throws Exception {
		CollectorRegistry registry = new CollectorRegistry();
		new ExtendedCollector().register(registry);
		MockCollectorScrapingContext scrapingContext = null;
		runTest(registry, scrapingContext, "defaultTarget");
	}
	
	
	protected void runTest(CollectorRegistry registry, CollectorScrapingContext scrapingContext, String sampleIdentification) {
		Enumeration<MetricFamilySamples> metricFamilySamples = registry.metricFamilySamples(scrapingContext);
		while (metricFamilySamples.hasMoreElements()) {
			Collector.MetricFamilySamples familySamples = (Collector.MetricFamilySamples) metricFamilySamples.nextElement();
			System.out.println(familySamples);
			assertTrue(familySamples.toString().contains(sampleIdentification));
		}
	}

	class ExtendedCollector extends Collector {


		@Override
		public List<MetricFamilySamples> collect() {
			return new ArrayList<Collector.MetricFamilySamples>();
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
