package test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			Socket s = new Socket("localhost", 8090);
			
			BufferedReader c = new BufferedReader(new InputStreamReader(System.in));
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			
			
			
//			String m = c.readLine();
////			String m = "HELLO BSI"+'\n';
//			m = m + '\n';
//			out.writeBytes(m);
//			System.out.println("Poslato serveru: "+m);
//			
//			String i = in.readLine();
//			System.out.println("Primljeno od servera: "+i);
			
			while(true) {
				String m = c.readLine();
//				String m = "HELLO BSI"+'\n';
				m = m + '\n';
				out.writeBytes(m);
				System.out.println("Poslato serveru: "+m);
				
				String i = in.readLine();
				System.out.println("Primljeno od servera: "+i);
			}
			
//			s.close();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
