package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

public class ClientWriter  implements Runnable {
	
	private final Socket connection;
	private final int ID;
	private final String myName;
	
	ClientWriter(Socket s, int ID, String name){
		this.connection = s;
		this.ID = ID;
		this.myName = name;
	}

	@Override
	public void run() {
		
	}
	
	public String getName() {
		return myName;
	}
	
	public void sendMessage(String message) throws IOException {
		PrintWriter out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
		
		out.print(message);
		
		out.flush();
		out.close();
	}
	
	
}
