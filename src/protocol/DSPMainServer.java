package protocol;

public class DSPMainServer implements DistributedSortingProtocol {
	
	public static final int WAITING = 0;
	public static final int ACCEPTED = 1;
	public static final int PEERS = 2;
	public static final int NOT_FOUND = 3;
	public static final int ERROR = 4;
	public static final int FINISHED = 5;
	public static final int DISCONNECT = 6;
	
	private int state;
	private String services;
	private String requests;
	
	public DSPMainServer() {
		this.state = DSPMainServer.WAITING;
		this.services = "";
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getServices() {
		return services;
	}
	
	public void setServices(String services) {
		this.services = services;
	}

	public String getRequests() {
		return requests;
	}

	public void setRequests(String requests) {
		this.requests = requests;
	}

	@Override
	public int parseProtocolMessage(String message) {
		// TODO Auto-generated method stub
		
		String[] messageParts = message.split(" ");
		String pMethod;
		String pOptions;
		
		if(messageParts.length == 1) {
			pMethod = messageParts[0];
			pOptions = "";
		} else if(messageParts.length == 2) {
			pMethod = messageParts[0];
			pOptions = messageParts[1];
		} else {
			pMethod = "";
			pOptions = "";
			setState(ERROR);
			return getState();
		}
		
		switch (pMethod) {
			
			case "HELLO": {
				
				if(pOptions.equals("B") || pOptions.equals("S") || pOptions.equals("I") || pOptions.equals("BS") ||
						pOptions.equals("BI") || pOptions.equals("SI") || pOptions.equals("BSI")) {
					setServices(pOptions);
					setState(DSPMainServer.ACCEPTED);
				} else {
					setServices("");
					setState(DSPMainServer.ERROR);
				}
			}
			break;
			
			case "PEERS": {
				
				if(pOptions.equals("B") || pOptions.equals("S") || pOptions.equals("I")) {
					setRequests(pOptions);
					setState(DSPMainServer.PEERS);
				} else {
					setRequests("");
					setState(DSPMainServer.NOT_FOUND);
				}
			}
			break;
			
			case "FINISHED": {
				
				setState(DSPMainServer.FINISHED);
			}
			break;
			
			case "BYE": {
				
				setState(DSPMainServer.DISCONNECT);
			}
			break;
			
			default:
				setState(DSPMainServer.ERROR);
		}
		
		return getState();
	}

	@Override
	public String generateResponse() {
		// TODO Auto-generated method stub
		String response = new String();
		
		if(getState() == DSPMainServer.ACCEPTED) {
			response = "WELCOME";
		}
		if(getState() == DSPMainServer.ERROR) {
			response = "ERROR";
		}
		if(getState() == DSPMainServer.NOT_FOUND) {
			response = "NOT_FOUND";
		}
		if(getState() == DSPMainServer.FINISHED) {
			response = "OK";
		}
		if(getState() == DSPMainServer.DISCONNECT) {
			response = "BYE";
		}
//		if(getState() == DSPMainServer.DISCONNECT) {
//			response = "";
//		}
		
		return response+'\n';
	}
	
	public String generateResponse(String message) {
		// TODO Auto-generated method stub
		String response = new String();
		
		if(getState() == DSPMainServer.PEERS) {
			response = "PEERS "+message;
		} else if(getState() == DSPMainServer.ERROR){
			response = "ERROR";
		} else {
			throw new RuntimeException("This method is used only when responding to PEERS request.");
		}
		
		return response+'\n';
	}

}
