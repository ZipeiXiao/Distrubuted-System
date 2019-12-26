/**
 * Author: Zipei (Tina) Xiao
 * Last Modified: Oct. 3rd, 2019
 *
 * The Tread includes the Key generator and other security methods,
 * i.e., methods other than the TCP socket transfer.
 */

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EchoServerThreadTCP extends Thread {
	private static BigInteger e;
	private static BigInteger n;
	private static List<User> users = new ArrayList<User>();
	Socket clientSocket = null;

	/**
	 * Constructor of this class
	 * New a Thread with the correct socket information
	 * @param clientSocket the TCP Socket
	 */
	public EchoServerThreadTCP(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	/**
	 * The server will make two checks before servicing any client request.
	 * First, does the public key (included with each request) hash to the ID
	 * (also provided with each request)?
	 * Second, is the request properly signed?
	 */
	public void run() {
		try {

			// Set up "in" to read from the client socket
			Scanner in;
			in = new Scanner(clientSocket.getInputStream());

			// Set up "out" to write to the client socket
			PrintWriter out;
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));

			/*
			 * Forever, read a line from the socket print it to the console echo it (i.e.
			 * write it) back to the client
			 */
			while (true) {
				String responseString = "Error in request";
				String requestString = in.nextLine();
				System.out.println("requestString: " + requestString);

				// The format of a request: "e;n;message;signed"
				String[] inputArray = requestString.split(";");
				if (inputArray.length == 4) {
					e = new BigInteger(inputArray[0]);
					n = new BigInteger(inputArray[1]);
					String message = inputArray[2];
					BigInteger signed = new BigInteger(inputArray[3]);
					BigInteger clear = signed.modPow(e, n);

					// Decode to a string
					String clearStr = new String(clear.toByteArray());

					// It is very important that the big integer created with the hash (before signing) is positive
					String hash = "00" + getHash(e.toString() + ";" + n.toString() + ";" + message);

					// Sign Verify
					if (clearStr.equals(hash)) {

						// ID, operation (add or subtract or view), and value
						inputArray = message.split(",");
						if (inputArray.length >= 2) {
							User user = FindUserByID(inputArray[0]);

							// ID Verify
							if (user.getID().equals(getID())) {

								// two checks for client request have both passed, i.e., the request
								//is carried out on behalf of the client.
								if (inputArray[1].equals("add")) {
									user.setSum(user.getSum() + Integer.valueOf(inputArray[2]));
									responseString = "OK";
								} else if (inputArray[1].equals("subtract")) {
									user.setSum(user.getSum() - Integer.valueOf(inputArray[2]));
									responseString = "OK";
								} else if (inputArray[1].equals("view")) {
									responseString = "Sum: " + user.getSum();
								}
							} else {

								// does not pass the second check
								responseString = "Error in request.";
							}
						}
					} else {

						// dose not pass the first check
						responseString = "Error in request.";
					}
				}
				System.out.println("Echoing: " + responseString);
				out.println(responseString);
				out.flush();
			}

		} catch (Exception e) {
			System.out.println("Exception:" + e.getMessage());

			// If quitting (typically by you sending quit signal) clean up sockets
		} finally {
			try {
				if (clientSocket != null) {
					clientSocket.close();
				}
			} catch (IOException e) {
				// ignore exception on close
			}
		}
	}

	/**
	 * Find the user by its ID
	 * @param ID input ID
	 * @return the user object
	 */
	private static User FindUserByID(String ID) {
		for (User user : users) {

			// user exist
			if (user.getID().equals(ID))
				return user;
		}
		// new user
		User user = new User();
		user.setID(ID);
		users.add(user);
		return user;
	}

	/**
	 * Note: an RSA public key is the pair e and n.
	 * The client's ID will be formed by taking the least significant 20 bytes of
	 * the hash of the client's public key.
	 * Prior to hashing, you might decide to combine these two integers with
	 * concatenation.
	 * @return ID
	 */
	public static String getID() {
		String hash = BabyHash.ComputeSHA_256_as_Hex_String((e.add(n)).toString());
		return hash.substring(0, 20);// last20BytesOf
	}

	/**
	 * Get the Hash value of the input string
	 * @param inStr input string
	 * @return the hash value
	 */
	public static String getHash(String inStr) {
		return BabyHash.ComputeSHA_256_as_Hex_String(inStr);
	}
}
