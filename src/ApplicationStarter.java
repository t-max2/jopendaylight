package jOpendaylight;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
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
		
		System.out.println(oc.getOpenFlowTableStatistics("00:00:00:00:00:00:00:01"));
	}
}
