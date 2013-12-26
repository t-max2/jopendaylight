package jOpendaylight;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApplicationStarter {
	public static void main(String[] args) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException {
		//	TODO
		String n3 = "00:00:00:00:00:00:00:03";
		String n4 = "00:00:00:00:00:00:00:04";
		OpendaylightClient oc = new OpendaylightClient();

		/*
		oc.createContainer("banana");
		
		oc.addNodeConnector("banana", new SimpleNodeConnector(n3, 1).toString());
		oc.addNodeConnector("banana", new SimpleNodeConnector(n3, 2).toString());
		oc.addNodeConnector("banana", new SimpleNodeConnector(n4, 1).toString());
		oc.addNodeConnector("banana", new SimpleNodeConnector(n4, 2).toString());
		*/
		/*
		oc.removeContainer("banana");
		oc.createContainer("banana");
		System.out.println(oc.getNodePropertiesOfContainer("banana"));
		*/
	}
}
