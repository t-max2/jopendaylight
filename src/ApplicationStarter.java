package jOpendaylight;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ApplicationStarter {
	public static void main(String[] args)
			throws JsonProcessingException, MalformedURLException, IOException, RuntimeException {
		//	TODO
		OpendaylightClient oc = new OpendaylightClient();
		//	oc.addUserLink("link4", "00:00:00:00:00:00:00:03", 1, "00:00:00:00:00:00:00:04", 2);
		//	oc.deleteUserLink("link2");
		//	oc.deleteUserLink("link4");
		//	System.out.println(oc.getUserLinks());
		
		//	System.out.println(oc.getHostDetails("10.0.0.1"));
		/*
		HashMap<String, String> paraMap;
		paraMap = new HashMap<String, String>();
		
		paraMap.put("dataLayerAddress", "00:00:00:00:01:01");
		paraMap.put("nodeType", "OF");
		paraMap.put("nodeId", "00:00:00:00:00:00:00:03");
		paraMap.put("nodeConnectorType", "OF");
		paraMap.put("nodeConnectorId", "1");
		paraMap.put("vlan", "1");
		paraMap.put("staticHost", "true");
		paraMap.put("networkAddress", "1.1.1.1");
		
		System.out.println(oc.addStaticHost("default", "1.1.1.1", paraMap));
		
		System.out.println(oc.getHostDetails("1.1.1.1"));
		*/
		
		//	oc.addOpenFlowStaticHost("1.1.1.1", "00:00:00:00:01:01", "00:00:00:00:00:00:00:03", 3, 1);
		//	System.out.println(oc.getActiveHosts());
		//	System.out.println(oc.getHostDetails("1.1.1.2"));
		
		//	System.out.println(oc.getHostDetails("1.1.1.2"));
		/*
		oc.deleteStaticHost("1.1.1.1");
		System.out.println(oc.getInactiveHosts());
		*/
	}
}
