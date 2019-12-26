/**
 * Author: Zipei (Tina) Xiao
 * Last Modified: Oct. 3rd, 2019
 *
 * This program demonstrates a very simple UDP client.
 * The user simply input a line of String and get the same echo
 * line form the server.
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

public class EchoClientUDP {
	public static void main(String args[]) {
		// Add a line at the top of the client so that it announces, by printing a message, "Client Running" at start up
		System.out.println("Client Running...");

		// args give message contents and server hostname
		DatagramSocket aSocket = null;
		try {
			// Change the client's “arg[0]” to a hardcoded "localhost".
			// build an InetAddress object from a DNS name
			InetAddress aHost = InetAddress.getByName("localhost");

			// the testing port is 6789
			int serverPort = 6789;

			// Constructs a datagram socket
			aSocket = new DatagramSocket();
			String nextLine;

			// The "standard" input stream. This stream is already open and ready to supply input data
			System.out.println("Try enter a string: ");
			BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));

			// user typed string
			while ((nextLine = typed.readLine()) != null) {
				byte[] m = nextLine.getBytes();

				// Constructs a datagram packet for sending with length, the
				// specified port number on the specified host and
				// byte array constructed from user input string
				DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);

				// Sends user typed string to server
				// send the Datagram on the socket
				// Sends a datagram packet from this socket.
				aSocket.send(request);

				// If the client enters the command "quit!", both the client and the server will halt execution.
				if (nextLine.equals("quit!")) {
					if (aSocket != null) {

						// When the client enters "quit!" it sends "quit!" to the sever but does not wait for any reply.
						// Closes this datagram socket
						aSocket.close();
						aSocket = null;

						// Add a line in the client so that it announces when it is quitting.
						System.out.println("Client: quitting...");
					}
					break;
				} else {

					// prepare room for the reply
					byte[] buffer = new byte[1000];

					// Constructs a DatagramPacket for receiving packets of length length.
					// build a datagram for the reply
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

					// receives a string from server
					// Receives a datagram packet (UDP style) from this socket.
					// block and wait
					aSocket.receive(reply);
					byte[] response = new byte[reply.getLength()];
					System.arraycopy(reply.getData(), 0, response, 0, reply.getLength());

					// show the result to the client
					System.out.println("Reply: " + new String(response));
					System.out.println("Try enter a string: ");
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

				// Closes this datagram socket
				aSocket.close();
		}
		System.out.println("Client: bye!");
	}
}
