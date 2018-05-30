import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class main {
    final static private String MY_NAME = "dindu";
    final static private String CENTRAL_SERVER_ADDRESS = "192.168.3.101";
    final static private int CENTRAL_SERVER_PORT = 5000;

	public static void main(String[] args) throws IOException, InterruptedException {
		while(true) {
            try {
				Socket central_server_socket = new Socket(CENTRAL_SERVER_ADDRESS, CENTRAL_SERVER_PORT);

                //readers starts listening on boot
                Thread readerP2P = new Thread(new ClientReaderP2P(central_server_socket));
                Thread udp_server = new Thread(new UDP_server());

                //open connections with central server
				Thread reader = new Thread(new ClientReader(central_server_socket));
				Thread writer = new Thread(new ClientWriter(central_server_socket, MY_NAME));

				reader.start();
				writer.start();
                readerP2P.start();
                udp_server.start();

				reader.join();
				writer.join();
                readerP2P.join();
                udp_server.join();
            } catch(ConnectException e) {
                System.err.println("Impossible conettere to server retrying");

			}
		}
	}
	
}
