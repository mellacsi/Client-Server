import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Random;

public class UDP_server implements Runnable {

	public UDP_server(){}

	@Override
	public void run() {
		try {
            DatagramSocket serverSocket = new DatagramSocket(6001);

            byte[] receiveData = new byte[1024];

            while(true)
            {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                String sentence = new String( receivePacket.getData());
                System.out.println("UDP_Server recieves: " + sentence);

                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();

                byte[] packet = new byte[1000];
                DatagramPacket sendPacket =
                        new DatagramPacket(packet, packet.length, IPAddress, port);

                Random random = new Random();
                Integer randomInt = random.nextInt(8);
                for(int i = 0; i < 1000; i++) packet[i] = randomInt.byteValue();

                for(int i = 0; i < 1000; i++) {
                    serverSocket.send(sendPacket);
                }

                System.out.println("UDP_Server sent all: ");
            }

		} catch (Exception e) {

		}
	}
}