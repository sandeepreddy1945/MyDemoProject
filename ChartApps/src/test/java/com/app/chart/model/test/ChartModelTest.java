/**
 * 
 */
package com.app.chart.model.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.app.chart.model.Chart;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author Sandeep
 *
 */
public class ChartModelTest {

	Random random = new Random();
	Chart chart;
	Chart.Animation animation;
	Chart.Connectors connectors;
	Chart.Node node;

	@Before
	public void setUp() {
		chart = new Chart();
		animation = chart.new Animation();
		connectors = chart.new Connectors();
		node = chart.new Node();
	}

	@Test
	public void testPojos() throws JsonProcessingException {
		setAnimationValues();
		setConnectorValues();
		setNodeValues();
		setSetters();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
		ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();
		System.out.println(objectWriter.writeValueAsString(chart));

	}

	private void setAnimationValues() {
		animation.setConnectorsAnimation(randomString());
		animation.setConnectorsSpeed(random.nextInt(10));
		animation.setNodeAnimation(randomString());
		animation.setNodeSpeed(random.nextInt(10));
	}

	private void setConnectorValues() {
		connectors.setType(randomString());
	}

	private void setNodeValues() {
		node.setCollapsable(true);
		node.setDrawLineThrough(false);
		node.setHtmlClass(randomString());
	}

	private String randomString() {
		return String.valueOf("asdfg" + random.nextInt(99999));
	}

	private void setSetters() {
		chart.setAnimateOnInit(true);
		chart.setAnimateOnInitDelay(random.nextInt(10));
		chart.setAnimation(animation);
		chart.setConnectors(connectors);
		chart.setNode(node);
		chart.setNodeAlign(randomString());
	}

	@Test
	public void testMemberJson() throws IOException {
		File file = File.createTempFile("member", "json");
		FileUtils.copyInputStreamToFile(chart.getClass().getClassLoader().getResourceAsStream("member.json"), file);
		String fileStr = FileUtils.readFileToString(file, Charset.defaultCharset());
		System.out.println(String.format(fileStr, randomString(), randomString(), randomString(), randomString(),
				randomString(), randomString()));
		file.delete();
		Assert.assertNotNull(fileStr);
	}

	@Test
	public void testChartConfigJson() throws IOException {
		File file = File.createTempFile("chartConfig", "json");
		FileUtils.copyInputStreamToFile(chart.getClass().getClassLoader().getResourceAsStream("chartConfig.json"),
				file);
		String fileStr = FileUtils.readFileToString(file, Charset.defaultCharset());
		Assert.assertNotNull(fileStr);
	}

	@Test
	public void testHeadMemberJson() throws IOException {
		File file = File.createTempFile("headMember", "json");
		FileUtils.copyInputStreamToFile(chart.getClass().getClassLoader().getResourceAsStream("headMember.json"), file);
		String fileStr = FileUtils.readFileToString(file, Charset.defaultCharset());
		System.out.println(String.format(fileStr, randomString(), randomString(), randomString(), randomString(),
				randomString(), randomString()));
		file.delete();
		Assert.assertNotNull(fileStr);
	}

}
