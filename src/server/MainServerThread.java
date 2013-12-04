package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.GregorianCalendar;

import protocol.DSPMainServer;

public class MainServerThread implements Runnable {

	private Socket communicationSocket;
	private boolean connectionTerminated;
	
	public MainServerThread(Socket socket) {
		this.communicationSocket = socket;
		this.connectionTerminated = false;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			System.out.println("Uspostavljena veza za klijentom "+this.communicationSocket.getInetAddress()+":"+this.communicationSocket.getPort()+
					", vreme"+(new GregorianCalendar()).getTime());
			String services;
			String response;
			
			Client client = new Client(communicationSocket.getInetAddress().toString(), communicationSocket.getPort());
			
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(communicationSocket.getInputStream()));
			DataOutputStream outputStream = new DataOutputStream(this.communicationSocket.getOutputStream());
			
			while(!this.connectionTerminated) {
				
				DSPMainServer protocol = new DSPMainServer();
				String clientMessage = inputStream.readLine();
//				System.out.println(clientMessage);
				if(clientMessage == null) {
					continue;
				}
				System.out.println("Primljena poruka od klijenta"+client.toString()+" (vreme "+(new GregorianCalendar()).getTime()+"): "+clientMessage);
				int responseCode = protocol.parseProtocolMessage(clientMessage);
				response = "";
				
//				if(responseCode == DSPMainServer.PEERS) {
//					services = MainServer.findTwoRandomClient(protocol.getRequests());
//					response = protocol.generateResponse(services); 
//				} else if(responseCode == DSPMainServer.ACCEPTED) {
//					Client client = new Client(communicationSocket.getInetAddress().toString(), communicationSocket.getPort(), protocol.getServices());
//					MainServer.addNewClient(client);
//					response = protocol.generateResponse();
//				}
				
				switch(responseCode) {
				
					case DSPMainServer.ACCEPTED: {
//						Client client = new Client(communicationSocket.getInetAddress().toString(), communicationSocket.getPort(), protocol.getServices());
						client.setServices(protocol.getServices());
						MainServer.addNewClient(client);
						response = protocol.generateResponse();
					}
					break;
					
					case DSPMainServer.PEERS: {
						services = MainServer.findTwoRandomClients(client.toString(), protocol.getRequests());
						response = protocol.generateResponse(services); 
					}
					break;
					
					case DSPMainServer.FINISHED: {
						System.out.println("Klijent "+client.toString()+" je uspesno zavrsio sortiranje.");
						response = protocol.generateResponse();
					}
					break;
					
					case DSPMainServer.ERROR: {
						this.connectionTerminated = true;
						MainServer.removeClient(client);
						response = protocol.generateResponse();
					}
					break;
					
					case DSPMainServer.DISCONNECT: {
						this.connectionTerminated = true;
						MainServer.removeClient(client);
						response = protocol.generateResponse();
					}
					break;
					
					default: {
						response = protocol.generateResponse();
					}
				}
				
				outputStream.writeBytes(response);
				System.out.println("Poslat odgovor klijentu "+client.toString()+": "+response);
				
			}
			this.communicationSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
