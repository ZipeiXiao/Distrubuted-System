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
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class EchoServerUDP {
	private static List<User> users = new ArrayList<User>();

    /**
     * This returned value from the server will be either “OK” or a value (if a view request was
     * made).
     * @param args
     */
	public static void main(String args[]) {
		System.out.println("Server Running...");
		DatagramSocket aSocket = null;
		byte[] buffer = new byte[1000];
		try {

            // the testing port is 6789
			aSocket = new DatagramSocket(6789);

            // build the packet holding the request from client
			DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			while (true) {
				aSocket.receive(request);
				String requestString = new String(request.getData());
				requestString = requestString.substring(0, request.getLength());
				System.out.println("requestString: " + requestString);

				// ID, operation (add or subtract or view), and value
				String[] inputArray = requestString.split(",");
				if (inputArray.length >= 2) {

				    // Assume that ID’s are positive integers.
					int ID = Integer.valueOf(inputArray[0]);

					// The server is given no prior knowledge of what ID’s will be transmitted to it by the client.
					User user = FindUserByID(ID);
					if (inputArray[1].equals("add")) {

                        // On the server, you will need to map each ID to the value of a sum.
						user.setSum(user.getSum() + Integer.valueOf(inputArray[2]));

                        // When the operation is “add”, the server performs the operation and simply returns “OK”.
						requestString = "OK";
					} else if (inputArray[1].equals("subtract")) {

                        // On the server, you will need to map each ID to the value of a sum.
						user.setSum(user.getSum() - Integer.valueOf(inputArray[2]));

                        // When the operation is “subtract” the server performs the operation and simply returns “OK”.
						requestString = "OK";
					} else if (inputArray[1].equals("view")) {

					    // When the operation is “view”, the value held on the server is returned.
						requestString = "Sum: " + user.getSum();
					}

					// The server will carry out the correct computation
                    // (add or subtract or view) using the ID found in each request.
					System.out.println("Echoing: " + requestString);

                    // prepare room for the response
					byte[] response = requestString.getBytes();

                    // build a datagram for the reply
					DatagramPacket reply = new DatagramPacket(response, response.length, request.getAddress(),
							request.getPort());

                    // send the result to the client
                    // send the Datagram on the socket
					aSocket.send(reply);
				}
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

	private static User FindUserByID(int ID) {
		for (User user : users) {

            // user exist
			if (user.getID() == ID)
				return user;
		}

		// new user
		User user = new User();
		user.setID(ID);
		users.add(user);
		return user;
	}
}
