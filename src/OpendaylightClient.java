package jOpendaylight;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OpendaylightClient {
	private final static String TYPE_OPENFLOW = "OF";
	private final static String TYPE_IPv4 = "0x800";
	
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
		return addUserLink(currentContainerName, name, TYPE_OPENFLOW, srcId, srcPort, dstId, dstPort);
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
	 * This class provides REST APIs to track host location in a network. 
	 * Host Location is represented by Host node connector which is essentially a logical entity that represents a Switch/Port. 
	 * A host is represented by it's IP-address and mac-address. 
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
		return addStaticHost(networkAddress, dataLayerAddress, TYPE_OPENFLOW, nodeId, nodePort, vlanId);
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
	
	/*
	 * Title:    Flow Configuration Northbound API
	 * Module:   org.opendaylight.controller.flowprogrammer.northbound.FlowProgrammerNorthbound
	 * Flow Configuration Northbound API provides capabilities to program flows. 
	 * API page: http://goo.gl/uEE7Jp
	 */
	
	//	Returns a list of Flows configured on the given container
	public JsonNode getAllStaticFlowEntries() throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getAllStaticFlowEntries(currentContainerName);
	}
	
	//	base method
	//	Returns a list of Flows configured on the given container
	public JsonNode getAllStaticFlowEntries(String containerName) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		String mountPoint = "/controller/nb/v2/flowprogrammer/" + containerName;
		return mapper.readTree(RestUtils.doGet(requestPrefix + mountPoint, userAccount, userPassword));
	}
	
	//	Returns a list of Flows configured on a Node in a given container
	public JsonNode getOpenFlowStaticFlowEntries(String nodeId) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getStaticFlowEntries(currentContainerName, TYPE_OPENFLOW, nodeId);
	}
	
	//	Returns a list of Flows configured on a Node in a given container
	public JsonNode getStaticFlowEntries(String nodeType, String nodeId) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getStaticFlowEntries(currentContainerName, nodeType, nodeId);
	}
	
	//	base method
	//	Returns a list of Flows configured on a Node in a given container
	public JsonNode getStaticFlowEntries(String containerName, String nodeType, String nodeId) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		String mountPoint = "/controller/nb/v2/flowprogrammer/" + containerName + "/node/" + nodeType + "/" + nodeId;
		return mapper.readTree(RestUtils.doGet(requestPrefix + mountPoint, userAccount, userPassword));
	}
	
	//	Returns the flow configuration matching a human-readable name and nodeId on a given Container.
	public JsonNode getOpenFlowStaticFlowEntry(String nodeId, String name) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getStaticFlowEntry(TYPE_OPENFLOW, nodeId, name);
	}
	
	//	Returns the flow configuration matching a human-readable name and nodeId on a given Container.
	public JsonNode getStaticFlowEntry(String nodeType, String nodeId, String name) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getStaticFlowEntry(currentContainerName, nodeType, nodeId, name);
	}
	
	//	base method
	//	Returns the flow configuration matching a human-readable name and nodeId on a given Container.
	public JsonNode getStaticFlowEntry(String containerName, String nodeType, String nodeId, String name) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		String mountPoint = "/controller/nb/v2/flowprogrammer/" + containerName + "/node/" + nodeType + "/" + nodeId + "/staticFlow/" + name;
		return mapper.readTree(RestUtils.doGet(requestPrefix + mountPoint, userAccount, userPassword));
	}
	
	//	Add or Modify a simple OpenFlow IPv4 flow configuration.
	//	If the flow exists already, it will replace the current flow.
	//	If operate success, returns String "Success"
	public String addOrModifySimpleFlowEntry(String name, String nodeId, String srcIp, String dstIp, int outputPort, Map<String, String> otherParaMap) throws ClientProtocolException, IOException{
		List<String> actionList;
		Map<String, String> paraMap;
		
		actionList = new ArrayList<String>();
		paraMap = new HashMap<String, String>();
		
		//	prepare actionList
		actionList.add("OUTPUT=" + outputPort);
		
		//	prepare paraMap
		paraMap.put("etherType", TYPE_IPv4);
		paraMap.put("nwSrc", srcIp);
		paraMap.put("nwDst", dstIp);
		
		//	put in other parameters
		if(otherParaMap != null){
			paraMap.putAll(otherParaMap);
		}
		
		return addOrModifyOpenFlowFlowEntry(name, nodeId, actionList, paraMap);
	}
	
	//	Add or Modify a flow configuration.
	//	If the flow exists already, it will replace the current flow.
	//	If operate success, returns String "Success"
	public String addOrModifyOpenFlowFlowEntry(String name, String nodeId, List<String> actionList, Map<String, String> otherParaMap) throws ClientProtocolException, IOException{
		return addOrModifyFlowEntry(name, TYPE_OPENFLOW, nodeId, actionList, otherParaMap);
	}
	
	//	Add or Modify a flow configuration. 
	//	If the flow exists already, it will replace the current flow.
	//	If operate success, returns String "Success"
	public String addOrModifyFlowEntry(String name, String nodeType, String nodeId, List<String> actionList, Map<String, String> otherParaMap) throws ClientProtocolException, IOException{
		return addOrModifyFlowEntry(currentContainerName, name, nodeType, nodeId, actionList, otherParaMap);
	}
	
	//	base method
	//	Add or Modify a flow configuration. 
	//	If the flow exists already, it will replace the current flow.
	//	If operate success, returns String "Success"
	public String addOrModifyFlowEntry(String containerName, String name, String nodeType, String nodeId, List<String> actionList, Map<String, String> otherParaMap) throws ClientProtocolException, IOException{
		String paraString;
		Map<String, String> nodeMap;
		Map<String, Object> requestMap;
		
		String mountPoint = "/controller/nb/v2/flowprogrammer/" + containerName + "/node/" + nodeType + "/" + nodeId + "/staticFlow/" + name;
		
		//	init
		nodeMap = new HashMap<String, String>();
		requestMap = new HashMap<String, Object>();
		
		//	put values 
		nodeMap.put("id", nodeId);
		nodeMap.put("type", nodeType);
		
		requestMap.put("name", name);
		requestMap.put("node", nodeMap);
		requestMap.put("actions", actionList);

		//	put all other parameters
		//	If parameters installed are different from paraMap, use ones in paraMap
		if(otherParaMap != null){
			requestMap.putAll(otherParaMap);
		}
		
		paraString = mapper.writeValueAsString(requestMap);	//	Map -> JSON String
		return RestUtils.doPut(requestPrefix + mountPoint, paraString, userAccount, userPassword);
	}
	
	//	Delete a Flow configuration
	//	If delete success, returns null
	public String deleteOpenFlowFlowEntry(String name, String nodeId) throws ClientProtocolException, IOException{
		return deleteFlowEntry(name, TYPE_OPENFLOW, nodeId);
	}
	
	//	Delete a Flow configuration
	//	If delete success, returns null
	public String deleteFlowEntry(String name, String nodeType, String nodeId) throws ClientProtocolException, IOException{
		return deleteFlowEntry(currentContainerName, name, nodeType, nodeId);
	}
	
	//	base method
	//	Delete a Flow configuration
	//	If delete success, returns null
	public String deleteFlowEntry(String containerName, String name, String nodeType, String nodeId) throws ClientProtocolException, IOException{
		String mountPoint = "/controller/nb/v2/flowprogrammer/" + containerName + "/node/" + nodeType + "/" + nodeId + "/staticFlow/" + name;
		return RestUtils.doDelete(requestPrefix + mountPoint, userAccount, userPassword);
	}
	
	//	Toggle a Flow configuration
	//	active <-> inactive
	//	If toggle success, return String "Success"
	public String toggleOpenFlowFlowEntry(String nodeId, String name) throws MalformedURLException, IOException, RuntimeException{
		return toggleFlowEntry(TYPE_OPENFLOW, nodeId, name);
	}
	
	//	Toggle a Flow configuration
	//	active <-> inactive
	//	If toggle success, return String "Success"
	public String toggleFlowEntry(String nodeType, String nodeId, String name) throws MalformedURLException, IOException, RuntimeException{
		return toggleFlowEntry(currentContainerName, nodeType, nodeId, name);
	}
	
	//	base method
	//	Toggle a Flow configuration
	//	active <-> inactive
	//	If toggle success, return String "Success"
	public String toggleFlowEntry(String containerName, String nodeType, String nodeId, String name) throws MalformedURLException, IOException, RuntimeException{
		String paraString = "";
		String mountPoint = "/controller/nb/v2/flowprogrammer/" + containerName + "/node/" + nodeType + "/" + nodeId + "/staticFlow/" + name;
		return RestUtils.doPost(requestPrefix + mountPoint, paraString, userAccount, userPassword);
	}
	
	/*
	 * Title:    Static Routing Northbound API
	 * Module:   org.opendaylight.controller.forwarding.staticrouting.northbound.StaticRoutingNorthbound
	 * Static Routing Northbound API allows for the management of the static routes.
	 * API page: http://goo.gl/aXHfOh
	 */
	
	//	Get a list of static routes present on the given container.
	public JsonNode getStaticRoutes() throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getStaticRoutes(currentContainerName);
	}
	
	//	base method
	//	Get a list of static routes present on the given container.
	public JsonNode getStaticRoutes(String containerName) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		String mountPoint = "/controller/nb/v2/staticroute/" + containerName + "/routes";
		return mapper.readTree(RestUtils.doGet(requestPrefix + mountPoint, userAccount, userPassword));
	}
	
	//	Returns the static route for the provided configuration name on a given container
	public JsonNode getStaticRoute(String routeName) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getStaticRoute(currentContainerName, routeName);
	}
	
	//	base method
	//	Returns the static route for the provided configuration name on a given container
	public JsonNode getStaticRoute(String containerName, String routeName) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		String mountPoint = "/controller/nb/v2/staticroute/" + containerName + "/route/" + routeName;
		return mapper.readTree(RestUtils.doGet(requestPrefix + mountPoint, userAccount, userPassword));
	}
	
	//	Add a new Static Route. 
	//	If a route by the given name already exists, 
	//	this method will return a non-successful status response.
	//	If operate success, return an empty String
	public String addStaticRoute(String routeName, String prefix, String nextHop) throws ClientProtocolException, IOException{
		return addStaticRoute(currentContainerName, routeName, prefix, nextHop);
	}
	
	//	Add a new Static Route. 
	//	If a route by the given name already exists, 
	//	this method will return a non-successful status response.
	//	If operate success, return an empty String
	public String addStaticRoute(String containerName, String routeName, String prefix, String nextHop) throws ClientProtocolException, IOException{
		Map<String, String> staticRouteData;
		staticRouteData = new HashMap<String, String>();
		
		staticRouteData.put("name", routeName);
		staticRouteData.put("prefix", prefix);
		staticRouteData.put("nextHop", nextHop);
		
		return addStaticRoute(containerName, routeName, staticRouteData);
		
	}
	
	//	Add a new Static Route. 
	//	If a route by the given name already exists, 
	//	this method will return a non-successful status response.
	//	If operate success, return an empty String
	public String addStaticRoute(String routeName, Map<String, String> staticRouteData) throws ClientProtocolException, IOException{
		return addStaticRoute(currentContainerName, routeName, staticRouteData);
	}
	
	//	base method
	//	Add a new Static Route. 
	//	If a route by the given name already exists, 
	//	this method will return a non-successful status response.
	//	If operate success, return an empty String
	public String addStaticRoute(String containerName, String routeName, Map<String, String> staticRouteData) throws ClientProtocolException, IOException{
		String paraString;
		String mountPoint = "/controller/nb/v2/staticroute/" + containerName + "/route/" + routeName;
		
		paraString = mapper.writeValueAsString(staticRouteData);	//	Map -> JSON String
		return RestUtils.doPut(requestPrefix + mountPoint, paraString, userAccount, userPassword);
	}
	
	//	Delete a Static Route
	//	If operate success, return null
	public String removeStaticRoute(String routeName) throws ClientProtocolException, IOException{
		return removeStaticRoute(currentContainerName, routeName);
	}
	
	//	base method
	//	Delete a Static Route
	//	If operate success, return null
	public String removeStaticRoute(String containerName, String routeName) throws ClientProtocolException, IOException{
		String mountPoint = "/controller/nb/v2/staticroute/" + containerName + "/route/" + routeName;
		return RestUtils.doDelete(requestPrefix + mountPoint, userAccount, userPassword);
	}
	
	/*
	 * Title:    Statistics REST APIs
	 * Module:   org.opendaylight.controller.statistics.northbound.StatisticsNorthbound
	 * Returns various Statistics exposed by the Southbound protocol plugins such as Openflow. 
	 * API page: http://goo.gl/n2n0kV
	 */
	
	//	Returns a list of all Flow Statistics from all the Nodes.
	public JsonNode getFlowStatistics() throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getFlowStatistics(currentContainerName);
	}
	
	//	base method
	//	Returns a list of all Flow Statistics from all the Nodes.
	public JsonNode getFlowStatistics(String containerName) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		String mountPoint = "/controller/nb/v2/statistics/" + containerName + "/flow";
		return mapper.readTree(RestUtils.doGet(requestPrefix + mountPoint, userAccount, userPassword));
	}
	
	//	Returns a list of all the Port Statistics across all the NodeConnectors on all the Nodes.
	public JsonNode getPortStatistics() throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getPortStatistics(currentContainerName);
	}
	
	//	base method
	//	Returns a list of all the Port Statistics across all the NodeConnectors on all the Nodes.
	public JsonNode getPortStatistics(String containerName) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		String mountPoint = "/controller/nb/v2/statistics/" + containerName + "/port";
		return mapper.readTree(RestUtils.doGet(requestPrefix + mountPoint, userAccount, userPassword));
	}
	
	//	Returns a list of all the Table Statistics on all Nodes.
	public JsonNode getTableStatistics() throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getTableStatistics(currentContainerName);
	}
	
	//	base method
	//	Returns a list of all the Table Statistics on all Nodes.
	public JsonNode getTableStatistics(String containerName) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		String mountPoint = "/controller/nb/v2/statistics/" + containerName + "/table";
		return mapper.readTree(RestUtils.doGet(requestPrefix + mountPoint, userAccount, userPassword));
	}
	
	//	Returns a list of Flow Statistics for a given Node.
	public JsonNode getOpenFlowFlowStatistics(String nodeId) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getFlowStatistics(TYPE_OPENFLOW, nodeId);
	}
	
	//	Returns a list of Flow Statistics for a given Node.
	public JsonNode getFlowStatistics(String nodeType, String nodeId) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getFlowStatistics(currentContainerName, nodeType, nodeId);
	}
	
	//	base method
	//	Returns a list of Flow Statistics for a given Node.
	public JsonNode getFlowStatistics(String containerName, String nodeType, String nodeId) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		String mountPoint = "/controller/nb/v2/statistics/" + containerName + "/flow/node/" + nodeType + "/" + nodeId;
		return mapper.readTree(RestUtils.doGet(requestPrefix + mountPoint, userAccount, userPassword));
	}
	
	//	Returns a list of all the Port Statistics across all the NodeConnectors in a given Node.
	public JsonNode getOpenFlowPortStatistics(String nodeId) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getPortStatistics(TYPE_OPENFLOW, nodeId);
	}
	
	//	Returns a list of all the Port Statistics across all the NodeConnectors in a given Node.
	public JsonNode getPortStatistics(String nodeType, String nodeId) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getPortStatistics(currentContainerName, nodeType, nodeId);
	}
	
	//	base method
	//	Returns a list of all the Port Statistics across all the NodeConnectors in a given Node.
	public JsonNode getPortStatistics(String containerName, String nodeType, String nodeId) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		String mountPoint = "/controller/nb/v2/statistics/" + containerName + "/port/node/" + nodeType + "/" + nodeId;
		return mapper.readTree(RestUtils.doGet(requestPrefix + mountPoint, userAccount, userPassword));
	}
	
	//	Returns a list of all the Table Statistics on a specific node.
	public JsonNode getOpenFlowTableStatistics(String nodeId) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getTableStatistics(TYPE_OPENFLOW, nodeId);
	}
	
	//	Returns a list of all the Table Statistics on a specific node.
	public JsonNode getTableStatistics(String nodeType, String nodeId) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		return getTableStatistics(currentContainerName, nodeType, nodeId);
	}
	
	//	base method
	//	Returns a list of all the Table Statistics on a specific node.
	public JsonNode getTableStatistics(String containerName, String nodeType, String nodeId) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		String mountPoint = "/controller/nb/v2/statistics/" + containerName + "/table/node/" + nodeType + "/" + nodeId;
		return mapper.readTree(RestUtils.doGet(requestPrefix + mountPoint, userAccount, userPassword));
	}
}
