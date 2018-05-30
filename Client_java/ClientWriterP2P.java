import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class ClientWriterP2P implements Runnable {

	private Socket socket;
	private String name = null;


	public ClientWriterP2P(Socket socket) {
		this.socket = socket;
	}

	public ClientWriterP2P(Socket socket, String name) {
		this.socket = socket;
		this.name = name;
	}
	
	String getName() {
		return name;
	}
	
	@Override
	public void run() {
		
		String to = null;

		try(PrintWriter out = new PrintWriter (new OutputStreamWriter(socket.getOutputStream()))){
			if(socket.isClosed()) return;

            Byte[] packet = new Byte[1000];

			Random random = new Random();
			Integer randomInt = random.nextInt(8);
			for(int i = 0; i < 1000; i++) packet[i] = randomInt.byteValue();

            for(int i = 0; i < 1000; i++) out.println(packet);
            out.flush();

            if(socket.isClosed()) return;
		} catch (IOException e1) {
			System.err.println("Connection lost: writerP2P");
		}	
	}
}
