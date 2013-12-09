package jOpendaylight;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

public class OpendaylightClient {
	private final String userAccount = "admin";
	private final String userPassword = "admin";
	private final String defaultContainerName = "default";
	
	private String controllerIp;
	private String requestPrefix;
	
	//	constructor
	public OpendaylightClient(String ip){
		this.controllerIp = ip;
		this.requestPrefix = "http://" + controllerIp + ":8080";
	}
	
	//	---------------------------
	//		API implementation
	//	---------------------------
	
	/*
	 * Title:    Topology REST APIs
	 * Module:   topology.northbound.TopologyNorthboundJAXRS
	 * API page: http://goo.gl/7fKnJR
	 */
	
	//	Retrieve the Topology
	public JSONObject getTopology() throws MalformedURLException, JSONException, IOException, RuntimeException{
		return getTopology(defaultContainerName);
	}
	
	//	Retrieve the Topology, base method
	public JSONObject getTopology(String containerName) throws MalformedURLException, JSONException, IOException, RuntimeException{
		String mountPoint = "/controller/nb/v2/topology/" + containerName;
		return new JSONObject(RestUtils.doGet(requestPrefix + mountPoint, userAccount, userPassword));
	}
}
