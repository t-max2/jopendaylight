package jOpendaylight;

public class SimpleNodeConnector {
	private final static String TYPE_OPENFLOW = "OF";
	
	private String type;
	private String nodeId;
	private int port;
	
	//	constructors
	public SimpleNodeConnector(String nodeId, int port) {
		this(TYPE_OPENFLOW, nodeId, port);
	}
	
	public SimpleNodeConnector(String type, String nodeId, int port) {
		this.type = type;
		this.nodeId = nodeId;
		this.port = port;
	}

	@Override
	public String toString() {
		return type + "|" + port + "@" + type + "|" + nodeId;
	}
}
