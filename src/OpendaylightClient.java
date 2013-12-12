package jOpendaylight;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

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
	public String addOpenFlowUserLink(String name, String srcId, int srcPort, String dstId, int dstPort) throws JsonProcessingException, ClientProtocolException, IOException{
		return addUserLink(currentContainerName, name, "OF", srcId, srcPort, dstId, dstPort);
	}
	
	//	Add an User Link
	public String addUserLink(String containerName, String name, String type, String srcId, int srcPort, String dstId, int dstPort) throws JsonProcessingException, ClientProtocolException, IOException{
		HashMap<String, String> paraMap = new HashMap<String, String>();

		paraMap.put("status", "Success");
		paraMap.put("name", name);
		paraMap.put("srcNodeConnector", type + "|" + srcPort + "@" + type + "|" + srcId);
		paraMap.put("dstNodeConnector", type + "|" + dstPort + "@" + type + "|" + dstId);
		
		return addUserLink(containerName, name, paraMap);
	}
	
	public String addUserLink(String name, Map<String, String> paraMap) throws JsonProcessingException, ClientProtocolException, IOException{
		return addUserLink(currentContainerName, name, paraMap);
	}
	
	//	Add an User Link, base method
	//	If add success, returns an empty String
	public String addUserLink(String containerName, String name, Map<String, String> paraMap) throws JsonProcessingException, ClientProtocolException, IOException{
		String paraString;
		String mountPoint = "/controller/nb/v2/topology/" + containerName + "/userLink/" + name;
		
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
	
	/*
	 * Title:    Host Tracker REST API
	 * Module:   org.opendaylight.controller.hosttracker.northbound.HostTrackerNorthbound
	 * API page: http://goo.gl/Spvfa7
	 */
	
	//	Returns a host that matches the IP Address value passed as parameter.
	public JsonNode getHostDetails(String networkAddress) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getHostDetails(currentContainerName, networkAddress);
	}
	
	//	base method
	//	Returns a host that matches the IP Address value passed as parameter.
	public JsonNode getHostDetails(String containerName, String networkAddress) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		String mountPoint = "/controller/nb/v2/hosttracker/" + containerName + "/address/"+ networkAddress;
		return mapper.readTree(RestUtils.doGet(requestPrefix + mountPoint, userAccount, userPassword));
	}
	
	//	Add a Static Host configuration. 
	//	If add successed, returns an empty String
	//	If a host by the given address already exists, this method will respond with a non-successful status response.
	public String addOpenFlowStaticHost(String networkAddress, String dataLayerAddress, String nodeId, int nodePort, int vlanId) throws ClientProtocolException, IOException{
		return addStaticHost(currentContainerName, networkAddress, dataLayerAddress, "OF", nodeId, nodePort, vlanId);
	}
	
	//	Add a Static Host configuration. 
	//	If add successed, returns an empty String
	//	If a host by the given address already exists, this method will respond with a non-successful status response.
	public String addStaticHost(String networkAddress, String dataLayerAddress, String type, String nodeId, int nodePort, int vlanId) throws ClientProtocolException, IOException{
		return addStaticHost(currentContainerName, networkAddress, dataLayerAddress, type, nodeId, nodePort, vlanId);
	}
	
	//	Add a Static Host configuration. 
	//	If add successed, returns an empty String
	//	If a host by the given address already exists, this method will respond with a non-successful status response.
	public String addStaticHost(String containerName, String networkAddress, String dataLayerAddress, String type, String nodeId, int nodePort, int vlanId) throws ClientProtocolException, IOException{
		HashMap<String, String> paraMap;
		paraMap = new HashMap<String, String>();
		
		paraMap.put("dataLayerAddress", dataLayerAddress);
		paraMap.put("nodeType", type);
		paraMap.put("nodeId", nodeId);
		paraMap.put("nodeConnectorType", type);
		paraMap.put("nodeConnectorId", String.valueOf(nodePort));
		paraMap.put("vlan", String.valueOf(vlanId));
		paraMap.put("staticHost", "true");
		paraMap.put("networkAddress", networkAddress);
		
		return addStaticHost(containerName, networkAddress, paraMap);
	}
	
	//	Add a Static Host configuration. 
	//	If add successed, returns an empty String
	//	If a host by the given address already exists, this method will respond with a non-successful status response.
	public String addStaticHost(String networkAddress, Map<String, String> paraMap) throws ClientProtocolException, IOException{
		return addStaticHost(currentContainerName, networkAddress, paraMap);
	}

	//	base method
	//	Add a Static Host configuration. 
	//	If add successed, returns an empty String
	//	If a host by the given address already exists, this method will respond with a non-successful status response.
	public String addStaticHost(String containerName, String networkAddress, Map<String, String> paraMap) throws ClientProtocolException, IOException{
		String paraString;
		String mountPoint = "/controller/nb/v2/hosttracker/" + containerName + "/address/"+ networkAddress;
		
		paraString = mapper.writeValueAsString(paraMap);	//	Map -> JSON String
		return RestUtils.doPut(requestPrefix + mountPoint, paraString, userAccount, userPassword);
	}
	
	//	Delete a Static Host configuration
	//	If delete successed, returns null
	public String deleteStaticHost(String networkAddress) throws ClientProtocolException, IOException{
		return deleteStaticHost(currentContainerName, networkAddress);
	}
	
	//	base method
	//	Delete a Static Host configuration
	//	If delete successed, returns null 
	public String deleteStaticHost(String containerName, String networkAddress) throws ClientProtocolException, IOException{
		String mountPoint = "/controller/nb/v2/hosttracker/" + containerName + "/address/"+ networkAddress;
		return RestUtils.doDelete(requestPrefix + mountPoint, userAccount, userPassword);
	}
	
	//	Returns a list of all Hosts : 
	//	both configured via PUT API and dynamically learnt on the network.
	public JsonNode getActiveHosts() throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getActiveHosts(currentContainerName);
	}
	
	//	base method
	//	Returns a list of all Hosts : 
	//	both configured via PUT API and dynamically learnt on the network.
	public JsonNode getActiveHosts(String containerName) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		String mountPoint = "/controller/nb/v2/hosttracker/" + containerName + "/hosts/active";
		return mapper.readTree(RestUtils.doGet(requestPrefix + mountPoint, userAccount, userPassword));
	}
	
	//	Returns a list of Hosts that are statically configured 
	//	and are connected to a NodeConnector that is down.
	public JsonNode getInactiveHosts() throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getInactiveHosts(currentContainerName);
	}
	
	//	base method
	//	Returns a list of Hosts that are statically configured 
	//	and are connected to a NodeConnector that is down.
	public JsonNode getInactiveHosts(String containerName) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		String mountPoint = "/controller/nb/v2/hosttracker/" + containerName + "/hosts/inactive";
		return mapper.readTree(RestUtils.doGet(requestPrefix + mountPoint, userAccount, userPassword));
	}
}
