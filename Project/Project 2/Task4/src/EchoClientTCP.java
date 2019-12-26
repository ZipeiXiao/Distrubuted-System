/**
 * Author: Zipei (Tina) Xiao
 * Last Modified: Oct. 4th, 2019
 *
 * This program demonstrates a very simple TCP client.
 *
 * The client may request either an “add” or “subtract” or
 * “view” operation be performed by the server. In addition,
 * each request will pass along an integer ID. Thus, the client
 * will form a packet with the following values: ID, operation (add or
 * subtract or view), and value (if the operation is other than view).
 * The program then blocks waiting for the server to perform
 * the requested operation. When the response packet arrives,
 * a String object is created and the reply is displayed.
 * The program illustrates the marshaling and un-marshaling
 * of requests and replies.
 */

// imports required for TCP/IP
import java.net.*;
import java.io.*;

public class EchoClientTCP {

	/**
	 * The client will display each returned value from the
	 * server to the user. This returned value will be either
	 * “OK” or a value (if a view request was made).
	 *
	 * @param args arguments supply hostname
	 */
	public static void main(String args[]) {
		System.out.println("Client Running...");
		//
		Socket clientSocket = null;
		try {

			// the testing port is 7777
			int serverPort = 7777;

			// build an Socket object from a DNS name and port 7777
			clientSocket = new Socket("localhost", serverPort);

			// The "standard" input stream. This stream is already open and ready to supply input data
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(
					new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
			String nextLine;
			BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));

			// the client will form a packet with the following values: ID, operation (add or
			// subtract or view), and value (if the operation is other than view).
			System.out.println("Enter your ID: ");
			String ID = typed.readLine();

			// repeatedly ask the user for the user ID, operation, and value (if not a view request)
			do {

				// The client side menu will provide an option to exit the client.
				System.out.println("Main menu:");
				System.out.println("1) add a value");
				System.out.println("2) subtract a value");
				System.out.println("3) view sum");

				// The client side menu will provide an option to exit the client.
				System.out.println("0) quit");
				nextLine = typed.readLine();

				if (nextLine.charAt(0) == '0') {
					if (clientSocket != null) {

						// Close the socket
						clientSocket.close();
						clientSocket = null;
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
				out.println(nextLine);
				out.flush();

				// read a line of data from the stream
				String data = in.readLine();

				// receives a string from server
				//The client will display each returned value from the server to the user.
				System.out.println("Received: " + data);
			} while (true);

			// handle general I/O exceptions
		} catch (IOException e) {
			System.out.println("IO Exception:" + e.getMessage());
		} finally {
			try {
				if (clientSocket != null)

					// Close the socket
					clientSocket.close();
			} catch (IOException e) {
				// ignore exception on close
			}
		}
		System.out.println("Client: bye!");
	}
}