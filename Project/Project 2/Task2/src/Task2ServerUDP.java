/**
 * Author: Zipei (Tina) Xiao
 * Last Modified: Oct. 3rd, 2019
 *
 * This program demonstrates a very simple UDP server.
 *
 * The packet is sent to the client asynchronously.
 * When the request packet arrives, a String object is created
 * and the partial sum is displayed.
 * The program illustrates the marshaling and un-marshaling
 * of requests and replies. It also demonstrates that UDP
 * style sockets are quite different from TCP style sockets.
 */

// imports required for UDP/IP
import java.net.*;
import java.io.*;

public class Task2ServerUDP {

	// The server will hold an integer value sum, initialized to 0.
	private static Integer sum = 0;

	/**
	 * The server is fever alive and is available for use.
	 * @param args
	 */
	public static void main(String args[]) {
		System.out.println("Server Running...");

		// define a Datagram (UDP style) socket
		DatagramSocket aSocket = null;
		byte[] buffer = new byte[1000];
		try {

			// the testing port is 6789
			aSocket = new DatagramSocket(6789);

			// receives requests from the client
			DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			while (true) {
				aSocket.receive(request);
				String requestString = new String(request.getData());
				requestString = requestString.substring(0, request.getLength());

				// requests from the client includes a value to be added to the sum.
				sum += Integer.valueOf(requestString);
				requestString = sum.toString();

				// On the server side console, upon each visit by the client, the new sum will be displayed.
				System.out.println("Echoing: " + requestString);

				// Upon each request, the server will return the new sum as a response to the client.
				byte[] response = requestString.getBytes();

				// build a datagram for the reply
				DatagramPacket reply = new DatagramPacket(response, response.length, request.getAddress(),
						request.getPort());

				// send the Datagram on the socket
				aSocket.send(reply);
			}

			// handle socket exceptions
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());

			// handle general I/O exceptions
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		}
//		finally {
//			if (aSocket != null)
//				aSocket.close();
//		}
	}
}
