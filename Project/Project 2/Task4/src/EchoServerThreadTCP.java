import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EchoServerThreadTCP extends Thread {
	private static List<User> users = new ArrayList<User>();
	Socket clientSocket = null;

	public EchoServerThreadTCP(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

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
				String requestString = in.nextLine();
				System.out.println("requestString: " + requestString);


				// ID, operation (add or subtract or view), and value
				String[] inputArray = requestString.split(",");
				if (inputArray.length >= 2) {
					User user = FindUserByID(inputArray[0]);
					if (inputArray[1].equals("add")) {

						// map each ID to the value of a sum.
						user.setSum(user.getSum() + Integer.valueOf(inputArray[2]));

						// When the operation is “add”, the server performs the operation and simply returns “OK”.
						requestString = "OK";
					} else if (inputArray[1].equals("subtract")) {

						// map each ID to the value of a sum.
						user.setSum(user.getSum() - Integer.valueOf(inputArray[2]));

						// When the operation is “subtract” the server performs the operation and simply returns “OK”.
						requestString = "OK";
					} else if (inputArray[1].equals("view")) {

						// When the operation is “view”, the value held on the server is returned.
						requestString = "Sum: " + user.getSum();
					}
					System.out.println("Echoing: " + requestString);
					out.println(requestString);
					out.flush();
				}
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
	 * Different ID’s may be
	 * presented and each will have its own sum.
	 *
	 * @param ID input string ID
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
}
