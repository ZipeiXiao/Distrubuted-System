/**
 * Author: Zipei (Tina) Xiao
 * Last Modified: Oct. 3rd, 2019
 *
 * This program demonstrates a very simple UDP client using
 * a proxy design to encapsulate the communication code.
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

public class EchoClientUDP {

	/**
	 * The client may request either an “add” or “subtract” or
	 * “view” operation be performed by the server.
	 * In addition, each request will pass along an integer ID.
	 *
	 * @param args args give message contents and server hostname
	 */
	public static void main(String args[]) {
		System.out.println("Client Running...");

		// define a Datagram (UDP style) socket
		DatagramSocket aSocket = null;
		try {

			// build an InetAddress object from a DNS name
			InetAddress aHost = InetAddress.getByName("localhost");

			// the testing port is 6789
			int serverPort = 6789;

			// Constructs a datagram socket
			aSocket = new DatagramSocket();
			String nextLine;

			// The "standard" input stream. This stream is already open and ready to supply input data
			BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));

			// the client will form a packet with the following values: ID, operation (add or
			// subtract or view), and value (if the operation is other than view).
			System.out.println("Enter your ID: ");
			String ID = typed.readLine();

			// repeatedly ask the user for the user ID, operation, and value (if not a view request)
			do {

				// The client is menu driven.
				System.out.println("Main menu:");
				System.out.println("1) add a value");
				System.out.println("2) subtract a value");
				System.out.println("3) view sum");

				// The client side menu will provide an option to exit the client.
				System.out.println("0) quit");
				nextLine = typed.readLine();

				if (nextLine.charAt(0) == '0') {
					if (aSocket != null) {

						// Closes this datagram socket
						aSocket.close();
						aSocket = null;
						System.out.println("Client: quitting...");
					}
					break;
				} else if (nextLine.charAt(0) == '1') {
					System.out.println("Try enter a vale to add: ");
					nextLine = ID + ",add," + typed.readLine();
				} else if (nextLine.charAt(0) == '2') {
					System.out.println("Try enter a vale to subtract: ");
					nextLine = ID + ",subtract," + typed.readLine();
				} else if (nextLine.charAt(0) == '3') {
					nextLine = ID + ",view";
				}

				byte[] m = nextLine.getBytes();

				// Constructs a datagram packet for sending packets of length length to the
				// specified port number on the specified host.
				DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);

				// Sends a datagram packet from this socket.
				// Sends user typed string to server
				aSocket.send(request);

				// prepare room for the reply
				byte[] buffer = new byte[1000];

				// Constructs a DatagramPacket for receiving packets of length length.
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

				// Receives a datagram packet from this socket.
				// receives a string from server
				aSocket.receive(reply);
				byte[] response = new byte[reply.getLength()];
				System.arraycopy(reply.getData(), 0, response, 0, reply.getLength());

				//The client will display each returned value from the server to the user.
				System.out.println("Reply: " + new String(response));
			} while (true);

			// handle socket exceptions
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());

			// handle general I/O exceptions
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)

				// Closes this datagram socket
				aSocket.close();
		}
		System.out.println("Client: bye!");
	}
}
