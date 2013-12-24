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

		/*
		oc.addOpenFlowNodeConnectorProperty("00:00:00:00:00:00:00:03", 2, "bandwidth", "1");
		//	oc.addOpenFlowNodeConnectorBandwidth("00:00:00:00:00:00:00:03", 1, 120000000000L);
		System.out.println(oc.getOpenFlowNodeConnectors("00:00:00:00:00:00:00:03"));
		*/
		
		//	oc.deleteOpenFlowNodeProperty("00:00:00:00:00:00:00:04", "name");
		
		oc.deleteOpenFlowNodeConnectorBandwidth("00:00:00:00:00:00:00:03", 2);
		//	oc.saveSwitchConfig();
		
		System.out.println(oc.getOpenFlowNodeConnectors("00:00:00:00:00:00:00:03"));
		//	System.out.println(oc.getNodes());
		//	oc.addOpenFlowNodeProperty("00:00:00:00:00:00:00:03", "description", "test1515");
		
		
	}
}
