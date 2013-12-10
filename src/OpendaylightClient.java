package jOpendaylight;

import java.io.IOException;
import java.net.MalformedURLException;

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
		return mapper.readTree(getTopology(currentContainerName));
	}
	
	//	Retrieve the Topology, base method
	public String getTopology(String containerName) throws JsonProcessingException, MalformedURLException, IOException, RuntimeException{
		String mountPoint = "/controller/nb/v2/topology/" + containerName;
		
		return RestUtils.doGet(requestPrefix + mountPoint, userAccount, userPassword);
	}
}
