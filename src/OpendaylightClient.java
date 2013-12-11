package jOpendaylight;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OpendaylightClient {
	private final static String defaultControllerIp = "127.0.0.1";
	private final static String defaultRestPort = "8080";
	
	private final static String defaultUserAccount = "admin";
	private final static String defaultUserPassword = "admin";
	
	private final static String defaultContainerName = "default";
	
	private String restPort;
	private String userAccount;
	private String userPassword;
	private String controllerIp;
	private String currentContainerName;
	
	private String requestPrefix;
	private ObjectMapper mapper;
	
	//	constructor with nothing
	public OpendaylightClient(){
		this(defaultControllerIp, defaultRestPort, defaultUserAccount, defaultUserPassword, defaultContainerName);
	}
	//	constructor with only ip
	public OpendaylightClient(String ip){
		this(ip, defaultRestPort, defaultUserAccount, defaultUserPassword, defaultContainerName);
	}

	//	base constructor
	public OpendaylightClient(String ip, String port, String account, String password, String container){
		controllerIp = ip;
		restPort = port;
		userAccount = account;
		userPassword = password;
		currentContainerName = container;
		
		requestPrefix = "http://" + controllerIp + ":" + restPort;
		mapper = new ObjectMapper();
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
	public JsonNode getTopology() throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getTopology(currentContainerName);
	}
	
	//	Retrieve the Topology, base method
	public JsonNode getTopology(String containerName) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		String mountPoint = "/controller/nb/v2/topology/" + containerName;
		
		return mapper.readTree(RestUtils.doGet(requestPrefix + mountPoint, userAccount, userPassword));
	}
	
	//	Retrieve the user configured links
	public JsonNode getUserLinks() throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getUserLinks(currentContainerName);
	}
	
	//	Retrieve the user configured links, base method
	public JsonNode getUserLinks(String containerName) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		String mountPoint = "/controller/nb/v2/topology/" + containerName + "/userLinks";
		
		return mapper.readTree(RestUtils.doGet(requestPrefix + mountPoint, userAccount, userPassword));
	}
	
	//	Add an User Link
	public String addUserLink(String name, String srcId, int srcPort, String dstId, int dstPort) throws JsonProcessingException, ClientProtocolException, IOException{
		return addUserLink(currentContainerName, name, "OF", srcId, srcPort, dstId, dstPort);
	}
	
	//	Add an User Link, base method
	//	If add success, returns an empty String
	public String addUserLink(String containerName, String name, String type, String srcId, int srcPort, String dstId, int dstPort) throws JsonProcessingException, ClientProtocolException, IOException{
		String paraString;
		HashMap<String, String> paraMap = new HashMap<String, String>();
		String mountPoint = "/controller/nb/v2/topology/" + containerName + "/userLink/" + name;

		paraMap.put("status", "Success");
		paraMap.put("name", name);
		paraMap.put("srcNodeConnector", type + "|" + srcPort + "@" + type + "|" + srcId);
		paraMap.put("dstNodeConnector", type + "|" + dstPort + "@" + type + "|" + dstId);
		
		paraString = mapper.writeValueAsString(paraMap);	//	Map -> JSON String
		
		return RestUtils.doPut(requestPrefix + mountPoint, paraString, userAccount, userPassword);
	}
	
	//	Delete an User Link
	public String deleteUserLink(String name) throws ClientProtocolException, IOException{
		return deleteUserLink(currentContainerName, name);
	}
	
	//	Delete an User Link, base method
	//	If delete OK, returns null
	public String deleteUserLink(String containerName, String name) throws ClientProtocolException, IOException{
		String mountPoint = "/controller/nb/v2/topology/" + containerName + "/userLink/" + name;
		return RestUtils.doDelete(requestPrefix + mountPoint, userAccount, userPassword);
	}
}
