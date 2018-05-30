import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientWriter implements Runnable {

	private final Socket socket;
	private final String name;

	public ClientWriter(Socket socket, String name) {
		this.socket = socket;
		this.name = name;
	}
	
	@Override
	public void run() {
		try(PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))){
			
			Scanner stdin = new Scanner(System.in);

			out.println("LOGIN:" + name);
			out.flush();
			System.out.println("login sent");

			System.out.println("nome destinatario:");
			String line;
			while((line = stdin.nextLine()) != null) {
                System.out.println("sent: " + line);
				out.println("SENDTO:" + line);
				out.flush();
			}
		} catch (IOException e1) {
			System.err.println("Connection lost: writer");
		}	
	}
}
