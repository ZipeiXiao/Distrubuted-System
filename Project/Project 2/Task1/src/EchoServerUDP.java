/**
 * Author: Zipei (Tina) Xiao
 * Last Modified: Oct. 3rd, 2019
 *
 * This program demonstrates a very simple UDP server.
 * The UDP Server merely echo back what it receives for the client,
 * unless it receives a "quit" request, it will run forever.
 * When the request arrives, a String object is created from the byte buffer array
 * and the request is displayed.
 * The program demonstrates that UDP style sockets are quite different from TCP style sockets.
 */

// imports required for UDP/IP
import java.net.*;
import java.io.*;

public class EchoServerUDP {

	/**
	 * The main method
	 * @param args
	 */
	public static void main(String args[]) {
		// Add a line at the top of the server so that it announces "Server Running" at start up.
		System.out.println("Server Running...");

		DatagramSocket aSocket = null;

		// The request data is copied to an array with the correct number of bytes.
		byte[] buffer = new byte[1000];
		try {
			// Constructs a datagram socket and binds it to the specified port on the local host machine.
			aSocket = new DatagramSocket(6789);

			// Constructs a datagram socket and binds it to the specified port on the local host machine.
			DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			while (true) {

				// Receives a datagram packet (UDP style) from this socket.
				aSocket.receive(request);

				// Constructs a datagram packet for sending packets of length length to the
				// specified port number on the specified host.
				byte[] response = new byte[request.getLength()];

				// Use the array of bytes of request to build a String of the correct size
				System.arraycopy(request.getData(), 0, response, 0, request.getLength());
				String requestString = new String(response);

				// If the client enters the command "quit!", both the client and the server will halt execution.
				if (requestString.equals("quit!")) {
					if (aSocket != null) {

						// Closes this datagram socket.
						aSocket.close();
						aSocket = null;

						// Add a line in the server so that it announces when it is quitting. The server only quits
						// when it is told to do so by the client.
						System.out.println("Server: quitting...");
					}
					break;
				} else {
					DatagramPacket reply = new DatagramPacket(response, request.getLength(), request.getAddress(),
							request.getPort());
					System.out.println("Echoing: " + requestString);

					// Sends a datagram packet from this socket.
					aSocket.send(reply);
				}
			}

		// handle socket exceptions
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());

			// handle general I/O exceptions
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());

			// always close the socket
		} finally {
			if (aSocket != null)

				// Closes this datagram socket.
				aSocket.close();
		}
		System.out.println("Server: bye!");
	}
}
