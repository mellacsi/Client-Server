import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class ClientReaderP2P implements Runnable {

	ServerSocket serverSocket;
	Socket client;

	public ClientReaderP2P(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
		serverSocket = new ServerSocket(6000);
		Socket client = serverSocket.accept();

			try(BufferedReader stream = new BufferedReader(new InputStreamReader(client.getInputStream()))){
				String line = null;

				//receive 1000 packets
                long startTime = 0, endTime;
				int count = 0;
				while(count < 1000){
					if((line = stream.readLine()) != null) {
					    if(count == 0) startTime = System.currentTimeMillis();
                        count++;
                    }
				}
                endTime = System.currentTimeMillis();

				//get address
				String senderAddress = client.getRemoteSocketAddress().toString();
                senderAddress = senderAddress.substring(1, senderAddress.length()-1);
                senderAddress =  senderAddress.split(":")[0];
                System.out.println("1000 TCP packets received from " + senderAddress + " in " + (endTime-startTime) + " milliseconds");

				//avvisa C2 che sei pronto x udp su 6001
                DatagramSocket clientSocket = new DatagramSocket();
                InetAddress IPAddress = InetAddress.getByName(senderAddress);

                byte[] sendData;
                String sentence = "I, AM, READY!!!";
                sendData = sentence.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 6001);
                clientSocket.send(sendPacket);

                byte[] receiveData = new byte[1000];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                count = 0;
                while(count < 1000){
                    clientSocket.receive(receivePacket);
                    if(count == 0) startTime = System.currentTimeMillis();
                    count++;
                }
                endTime = System.currentTimeMillis();

                System.out.println("1000 UDP packets received from " + senderAddress + " in " + (endTime-startTime) + " milliseconds");

                clientSocket.close();

			} catch (IOException e1) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {

				}
			}
		} catch (Exception e) {

		}
	}
}