package pirateGooseClient;


public class ClientHandler {

	private String key;
	private String ip;
	private Integer id;
	private String rmiURI;
	private int port;
	private int serviceID;

	public ClientHandler(String key, String ip, Integer id) {
		this.key = key;
		this.ip = ip;
		this.id = id;
		this.port = 1100 + id;
		this.rmiURI = "rmi://" + ip + ":" + port + "/pirateGoose"+key; 
	}

	public int getServiceID() {
		return serviceID;
	}

	public void setServiceID(int serviceID) {
		this.serviceID = serviceID;
	}

	public String getRmiURI() {
		return rmiURI;
	}

	public String getKey() {
		return key;
	}

	public String getIp() {
		return ip;
	}

	public int getId() {
		return id;
	}

}
