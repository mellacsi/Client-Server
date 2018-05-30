package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;

public class ClientListener implements Runnable {
	
	private final Socket connection;
	private final int ID;
	private String myName;
	private String myIP;
	
	ClientListener(Socket s, int ID){
		this.myName = "DEFAULTNAME" + ID;
		this.connection = s;
		this.ID = ID;
		myIP = s.getRemoteSocketAddress().toString().split(":")[0];//.toString();
	}

	@Override
	public void run() {
		System.out.println("Client " + ID + " waiting for LOGIN " + myIP );
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			String destinatario = null;
			
			String s;
			try {
				while ( (s = in.readLine())!=null ) {
					s = s.trim();
					//System.out.println("\tClient -" + ID + "- elaboro: " + s);
					if(s.contains("LOGIN:")) {
						String name =  s.substring(s.indexOf(":")+1, s.length()).trim();
						
						if(!ServerListener.isConnected(name)) {
							myName = name;
							System.out.println("LOGIN SUCESFULLY FOR: -" + myName + "- " + myIP);
							ServerListener.sendMessageTo(myName, "LOGIN:SUCESSFULLY Thank for your personal data");//226
						}else {
							System.err.println("LOGIN NOT SUCESFULLY FOR:" + "Already used" + myIP);
							ServerListener.sendMessageTo(myName, "ERROR:418_Username Already in use");//226
						}
						
						
					}else if(s.contains("SENDTO:")) {
						destinatario = s.substring(s.indexOf(":")+1, s.length()).trim();
						
						System.out.println("Client " + myName + " cerca IP DI: -" + destinatario + "-");
						
						String ipDestinatario = ServerListener.getIPof(destinatario);
						
						if(ipDestinatario==null) {
							
							System.err.println("Mando a "  + myName + " l'IP destinatario NON TROVATO");
							ServerListener.sendMessageTo(myName, "ERROR:404NotFound");
						}else {
							System.out.println("Mando a "  + myName + " l'IP destinatario " + ipDestinatario);
							
							ServerListener.sendMessageTo(myName, "SENDTO:"  + ipDestinatario.toString());
							
							System.out.println("Mando a "  + myName + " l'IP destinatario con successo");
						}
					}else if(s.contains("QUIT:")) {
						//TODO END CONNECTION
						ServerListener.sendMessageTo(myName, "QUIT:Sucessfully");
						break;
					}else {
						System.out.println("Mando a "  + myName + " Messaggio non compreso ");
						
						ServerListener.sendMessageTo(myName, "ERROR:418 I Cannot understand the messagge \"" + s + "\"");
					}
				}
				System.out.println("Server chiude la connession");
				in.close();
				
			}catch (SocketException error) {
				disconnect();
				System.err.println("client odpojen");
			}
			
		} catch (IOException e) {
			disconnect();
			e.printStackTrace();
		}
		
		disconnect();
	}
	
	private void disconnect() {
		ServerListener.removeClientListener(this);
		myName = "DISCONNETTED";
	}
	
	public String getName() {
		return myName;
	}

	public synchronized Socket getSocket() {
		return connection;
	}
	
	public synchronized String getIP() {
		return myIP;
	}
	
	
}
