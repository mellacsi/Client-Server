package server;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerListener {
	static int id = 0;
	static ArrayList<ClientListener> threads = new ArrayList<>();
	
	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(5000); // Il server si mette in ascolto
		while(true)
			try {
				System.out.println("In attesa di connessione...");
				Socket client = server.accept();
				
				ClientListener runnable = new ClientListener(client, id++);
				Thread newThread = new Thread(runnable);
				
				threads.add(runnable);				
				newThread.start();
			}
			catch (IOException ioe) {
	
			}
	}
	
	public static synchronized void removeClientListener(ClientListener toRemove) {
		System.out.println("RIMUOVO " + toRemove.getName());
		threads.remove(toRemove);
	}
		
	public static synchronized boolean sendMessageTo(final String destinatario, final String message) throws IOException {
		System.out.println("Server cerca di mandare messaggio a " + destinatario);
		for(ClientListener miao : threads)
			if(miao.getName()!=null && miao.getName().equals(destinatario)) {
				PrintWriter out = new PrintWriter(new OutputStreamWriter(miao.getSocket().getOutputStream()));			
				
				System.out.println("Server invio messaggio: " + message + " a " + destinatario );
				out.println(message);
				out.flush();
				return true;
			}
		return false;
	}	
	
	
	public static synchronized String getIPof(final String name) {		
		for(ClientListener miao : threads) {
			if(miao.getName()==null) {
				continue;
			}
			if(miao.getName().equals(name)) {
				return miao.getIP();
			}
		}
		return null;
	}
	
	public static synchronized boolean isConnected(String name) {
		for(ClientListener miao : threads) 
			if(miao.getName().equals(name)) 
				return true;
		return false;
	}
}
