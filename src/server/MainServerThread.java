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
			System.out.println("(vreme: "+(new GregorianCalendar()).getTime()+") "+"Uspostavljena veza za klijentom "+this.communicationSocket.getInetAddress()+" na portu "+this.communicationSocket.getPort());
			String services;
			String response;
			
			Client client = new Client(communicationSocket.getInetAddress().toString());
			
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(communicationSocket.getInputStream()));
			DataOutputStream outputStream = new DataOutputStream(this.communicationSocket.getOutputStream());
			
			while(!this.connectionTerminated) {
				
				DSPMainServer protocol = new DSPMainServer();
				String clientMessage = inputStream.readLine();
				if(clientMessage == null) {
					continue;
				}
				System.out.println("(vreme: "+(new GregorianCalendar()).getTime()+") "+"Primljena poruka od klijenta "+client.getIpAddress()+" na portu "+communicationSocket.getPort()+": "+clientMessage);
				int responseCode = protocol.parseProtocolMessage(clientMessage);
				response = "";
				
				switch(responseCode) {
				
					case DSPMainServer.ACCEPTED: {
						client.setPort(protocol.getPort());
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
						System.out.println("(vreme: "+(new GregorianCalendar()).getTime()+") "+"Klijent "+client.getIpAddress()+" na portu "+communicationSocket.getPort()+" je uspesno zavrsio sortiranje");
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
				System.out.println("(vreme: "+(new GregorianCalendar()).getTime()+") "+"Poslat odgovor klijentu "+client.getIpAddress()+" na portu "+communicationSocket.getPort()+" : "+response);
				
			}
			this.communicationSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
