package server;

public class Client {

	private String ipAddress;
	private int port;
	private String services;
	private String data;
	
	public Client(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public Client(String ipAddress, int port) {
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public Client(String ipAddress, int port, String services) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.services = services;
	}
	
	public Client(String ipAddress, int port, String services, String data) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.services = services;
		this.data = data;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getServices() {
		return services;
	}

	public void setServices(String services) {
		this.services = services;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Client))
			return false;
		Client other = (Client) obj;
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	public String toString() {
		return getIpAddress().toString()+":"+getPort();
	}
	
}
