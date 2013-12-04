package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.Random;

public class MainServer {

	private static LinkedList<Client> clients = new LinkedList<>();
	
	public static void addNewClient(Client client) {
		clients.add(client);
		for(int i=0; i<clients.size(); i++) {
			System.out.println(clients.get(i).toString()+"-"+clients.get(i).getServices());
		}
		System.out.println('\n');
	}
	
	public static void removeClient(Client client) {
		clients.remove(client);
		for(int i=0; i<clients.size(); i++) {
			System.out.println(clients.get(i).toString()+"-"+clients.get(i).getServices());
		}
		System.out.println('\n');
	}
	
	public static String findTwoRandomClients(String client, String request) {
		int l = 0;
		int u = clients.size();
		int[] visited = new int[u+1];
		int found = 0;
		visited[0] = -1;
		String response = "";
		Random r = new Random();
		for(int i=0; i<clients.size(); i++) {
			int rnd = r.nextInt(u - l) + l;
			
			for(int j=0; j<=i; j++) {
				if(visited[j]==rnd) {
					rnd = r.nextInt(u-l)+l;
					j=-1;
				}
			}
			
			visited[i+1] = rnd;
			if(!clients.get(rnd).toString().equals(client) && clients.get(rnd).getServices().contains(request)) {
				response = response + clients.get(rnd).toString();
				found++;
			}
			if(found==2) {
				break;
			}
		}
		if(found==0) {
			return "NOT_FOUND";
		}
		return response;
	}
	
	/**
	 * @param args
	 */
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ServerSocket mainServer;
		int port = 8090;
		
		try {
			mainServer = new ServerSocket(port);
			System.out.println("Server osluskuje na portu "+port+"...");
			while(true) {
				new Thread(new MainServerThread(mainServer.accept())).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
