/**
 * Author: Zipei (Tina) Xiao
 * Last Modified: Oct. 3rd, 2019
 *
 * This program demonstrates a very simple UDP client.
 *
 * The packet is sent to the server asynchronously.
 * The program then blocks waiting for the server to perform
 * the requested operation. When the response packet arrives,
 * a String object is created and the reply is displayed.
 * The program illustrates the marshaling and un-marshaling
 * of requests and replies. It also demonstrates that UDP
 * style sockets are quite different from TCP style sockets.
 */

// imports required for UDP/IP
import java.net.*;
import java.io.*;

public class Task2ClientUDP {

	/**
	 * The main method of the client will have
	 * no code related to interacting with a server. Instead, the main routine will simply call a
	 * local method named “add”. Since we are using a proxy design, client side main routine will call
	 * its local “add” method 1000 times.
	 * @param args args give message contents and server hostname
	 */
	public static void main(String args[]) {
		System.out.println("Client Running...");

		// sends 1000 messages to your server in order to compute the sum
		// 1+2+3+..+1000
		for (int i = 0; i < 1000; i++) {
			System.out.println("add: " + i);
			add(i);
		}
	}

	/**
	 * On the client, all of the communication code will be
	 * placed in a method named “add”. The “add” method will not perform any addition, instead, it
	 * will request that the server perform the addition. The “add” method will encapsulate or
	 * hide all communication with the server. It is within the “add” method where we actually
	 * work with sockets. This is a variation of what is called a “proxy design”. The “add”
	 * method is serving as a proxy for the server.
	 * @param number input integer to be added
	 */
	private static void add(int number) {

		// define a Datagram (UDP style) socket
		DatagramSocket aSocket = null;
		try {
			String nextLine = number + "";
			byte[] m = nextLine.getBytes();

			// build an InetAddress object from a DNS name
			InetAddress aHost = InetAddress.getByName("localhost");

			// the testing port is 6789
			int serverPort = 6789;
			aSocket = new DatagramSocket();

			// build the packet holding the destination address, port and
			// byte array constructed from the user input number
			DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);

			// The “add” method actually sends the message to the server.
			// send the Datagram on the socket
			aSocket.send(request);

			// prepare room for the reply
			byte[] buffer = new byte[1000];

			// build a datagram for the reply
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

			// block and wait
			aSocket.receive(reply);

			// Display the final result to the user on the client side.
			System.out.println("Sum: " + new String(reply.getData()));

			// handle socket exceptions
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());

			// handle general I/O exceptions
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {

			// always close the socket
			if (aSocket != null)
				aSocket.close();
		}
	}
}
