package jOpendaylight;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApplicationStarter {
	public static void main(String[] args)
			throws JsonProcessingException, MalformedURLException, IOException, RuntimeException {
		//	TODO
		OpendaylightClient oc = new OpendaylightClient();

		//	System.out.println(oc.getStaticRoute("route1"));
		/*
		Map<String, String> para;
		para = new HashMap<String, String>();
		
		para.put("name", "route1");
		para.put("prefix", "10.10.1.0/24");
		para.put("nextHop", "1.1.1.1");
		
		System.out.println(oc.addStaticRoute("route1", para));
		*/
		
		//	oc.addStaticRoute("route2", "10.10.1.0/24", "1.1.1.1");
		//	oc.removeStaticRoute("route2");
		
		//	System.out.println(oc.getOpenFlowTableStatistics("00:00:00:00:00:00:00:01"));
		/*
		JsonNode topo = oc.getTopology();
		JsonNode edgeProperties = topo.get("edgeProperties");
		
		//	System.out.println(edgeProperties.toString());
		
		for(JsonNode edgePropertie: edgeProperties){
			System.out.println(edgePropertie.get("edge").toString());
		}
		*/
		
		//	System.out.println(oc.listSubnets());
		
		//	System.out.println(oc.getTopology());
		//	oc.addOpenFlowUserLink("linkTest", "00:00:00:00:00:00:00:03", 2, "00:00:00:00:00:00:00:04", 1);
		/*
		ArrayList<String> nodeConnectors = new ArrayList<String>();
		nodeConnectors.add("OF|2@OF|00:00:00:00:00:00:00:03");
		nodeConnectors.add("OF|1@OF|00:00:00:00:00:00:00:04");
		
		System.out.println(oc.addSubnet("sn1", "10.0.0.1/8", nodeConnectors));
		
		System.out.println(oc.listSubnets());
		*/
		/*
		ArrayList<String> nodeConnectors = new ArrayList<String>();
		nodeConnectors.add(new SimpleNodeConnector("00:00:00:00:00:00:00:03", 2).toString());
		nodeConnectors.add(new SimpleNodeConnector("00:00:00:00:00:00:00:04", 1).toString());
		*/
		/*
		System.out.println(oc.modifySubnet("sn1", "10.0.0.1/8", null));
		
		System.out.println(oc.listSubnets());
		*/
		
		//	System.out.println(oc.removeSubnet("sn1"));
		//	System.out.println(oc.toggleOpenFlowFlowEntry("00:00:00:00:00:00:00:03", "flow1"));
		
	}
}
