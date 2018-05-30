import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
public class ClientReader implements Runnable {

    Socket socket;

    public ClientReader(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String line;

            while (true) {
                if ((line = in.readLine()) != null) {
                    System.out.println("recieved from server: " + line);

                    if (line.contains("SENDTO:")) {
                        String address = line.split("/")[1];
                        System.out.println("address recieved: " + address);

                        Socket socket = new Socket(address, 6000);

                        Thread writer = new Thread(new ClientWriterP2P(socket));


                        writer.start();

                        try {
                            writer.join();
                        } catch (Exception e) {

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}