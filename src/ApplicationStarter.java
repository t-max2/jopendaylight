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
	public static void main(String[] args)
			throws JsonProcessingException, MalformedURLException, IOException, RuntimeException {
		//	TODO
		OpendaylightClient oc = new OpendaylightClient();

		/*
		oc.removeContainer("red");
		oc.removeContainer("blue");
		oc.removeContainer("banana");
		
		System.out.println(oc.viewAllContainers());
		*/
		/*
		
		System.out.println(oc.viewContainerFlowSpecs("banana"));
		*/
		
		
		//	System.out.println(oc.viewAllContainers());
		
		//	oc.createContainer("banana");
		
		/*
		List<String> ncs = new ArrayList<String>();
		ncs.add(new SimpleNodeConnector("00:00:00:00:00:00:00:02", 3).toString());
		ncs.add(new SimpleNodeConnector("00:00:00:00:00:00:00:04", 4).toString());
		
		oc.addNodeConnectors("default", ncs);
		System.out.println(oc.viewContainer("default"));
		*/
		
		//	oc.removeContainer("banana");
		/*
		Map<String, String> paraMap = new TreeMap<String, String>();
		paraMap.put("name", "httpEx");
		paraMap.put("nwSrc", "10.0.0.1");
		paraMap.put("nwDst", "10.0.0.2");
		
		
		oc.createFlowSpec("banana", "http", paraMap);
		System.out.println(oc.viewContainerFlowSpec("banana", "http"));
		*/
		/*
		oc.removeFlowSpec("banana", "http");
		System.out.println(oc.viewContainer("banana"));
		*/
		
		oc.removeContainer("banana");
	}
}
