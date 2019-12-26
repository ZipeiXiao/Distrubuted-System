/**
 * Author: Zipei (Tina) Xiao
 * Last Modified: Oct. 4th, 2019
 *
 * This program demonstrates a very simple TCP client.
 *
 * The client may request either an “add” or “subtract” or
 * “view” operation be performed by the server. In addition,
 * each request will pass along an integer ID.
 * During one client session, the ID will always be the same.
 * If the client quits and restarts,
 * it will have a new ID and operate on a new sum.
 */

// imports required for TCP/IP
import java.net.*;
import java.util.Random;
import java.io.*;
import java.math.BigInteger;

public class EchoClientTCP {

	// the public key: e and n
	private static BigInteger e;

	// d and n - the client’s private key
	private static BigInteger d;
	private static BigInteger n;

	/**
	 * Note: an RSA public key is the pair e and n.
	 * The client's ID will be formed by taking the least significant 20 bytes of
	 * the hash of the client's public key.
	 * Prior to hashing, you might decide to combine these two integers with
	 * concatenation.
	 *
	 * @return a SHA_256_as_Hex_String
	 */
	public static String getID() {
		String hash = BabyHash.ComputeSHA_256_as_Hex_String((e.add(n)).toString());

		// last20BytesOf
		return hash.substring(0, 20);
	}

	/**
	 * Get the Hash value of a string
	 * @param inStr input string
	 * @return Hash value
	 */
	public static String getHash(String inStr) {
		return BabyHash.ComputeSHA_256_as_Hex_String(inStr);
	}

	/**
	 * The client will sign each request. So, by using its private key (d
	 * and n), the client will encrypt the hash of the message it sends to the
	 * server. The signature will be added to each request. It is very important
	 * that the big integer created with the hash (before signing) is positive. See
	 * BabySign and BabyVerify for details.
	 *
	 * @param inStr input Hash value of a message
	 * @return Signed request
	 */
	private static String getSignedHashOfMessage(String inStr) {

		// m is the original clear text
		BigInteger m = new BigInteger(inStr.getBytes());

		// by using its private key (d and n), the client encrypt the hash of the message it sends to the server.
		BigInteger c = m.modPow(d, n);
		return c.toString();
	}

	/**
	 * Initialize the RSA
	 */
	private static void RSAInit() {
		Random rnd = new Random();

		// Step 1: Generate two large random primes.
		// We use 400 bits here, but best practice for security is 2048 bits.
		// Change 400 to 2048, recompile, and run the program again and you will
		// notice it takes much longer to do the math with that many bits.
		BigInteger p = new BigInteger(400, 100, rnd);
		BigInteger q = new BigInteger(400, 100, rnd);

		// Step 2: Compute n by the equation n = p * q.
		n = p.multiply(q);

		// Step 3: Compute phi(n) = (p-1) * (q-1)
		BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

		// Step 4: Select a small odd integer e that is relatively prime to phi(n).
		// By convention the prime 65537 is used as the public exponent.
		e = new BigInteger("65537");

		// Step 5: Compute d as the multiplicative inverse of e modulo phi(n).
		d = e.modInverse(phi);

		System.out.println(" e = " + e.toString()); // Step 6: (e,n) is the RSA public key
		System.out.println(" d = " + d.toString()); // Step 7: (d,n) is the RSA private key
		System.out.println(" n = " + n.toString()); // Modulus for both keys
	}

	/**
	 * The client will send the id: last20BytesOf(h(e+n)), the public key: e and n in the clear,
	 * the operation (add, view, or subtract), the operand, and the signature
	 * E(h(all prior tokens),d). The signature is thus an encrypted hash. It is encrypted
	 * using d and n - the client’s private key. E represents standard RSA encryption. The
	 * function h(e+n) is the hash of e concatenated with n.
	 * Use a proxy design to encapsulate the communication code.
	 *
	 * @param args
	 */
	public static void main(String args[]) {
		System.out.println("Client Running...");

		// Each time the client runs, it will create new RSA public and private keys.
		RSAInit();

		// arguments supply hostname
		Socket clientSocket = null;
		try {
			int serverPort = 7777;
			clientSocket = new Socket("localhost", serverPort);

			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(
					new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
			String message;
			BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
			String ID = getID();
			System.out.println("ID: " + ID);
			do {

				// the client is interactive and menu driven.
				System.out.println("Main menu:");
				System.out.println("1) add a value");
				System.out.println("2) subtract a value");
				System.out.println("3) view sum");

				// an option to exit.
				System.out.println("0) quit");
				message = typed.readLine();

				if (message.charAt(0) == '0') {
					if (clientSocket != null) {
						clientSocket.close();
						clientSocket = null;
						System.out.println("Client: quitting...");
					}
					break;
				} else if (message.charAt(0) == '1') {
					System.out.println("Try enter a vale to add: ");

					// transmit add requests to the server, along with the ID computed
					message = ID + ",add," + typed.readLine();
				} else if (message.charAt(0) == '2') {
					System.out.println("Try enter a vale to subtract: ");

					// transmit subtract requests to the server, along with the ID computed
					message = ID + ",subtract," + typed.readLine();
				} else if (message.charAt(0) == '3') {

					// transmit view requests to the server, along with the ID computed
					message = ID + ",view";
				}

				// The client will also transmit its public key with each request.
				// Again, note that this key is a combination of e and n.
				// The format of a request: "e;n;message;signed"
				message = e.toString() + ";" + n.toString() + ";" + message;

				// It is very important that the big integer created with the hash (before
				// signing) is positive
				String hash = "00" + getHash(message);

				// the client will sign each request. The signature will be added to each request.
				out.println(message + ";" + getSignedHashOfMessage(hash));
				out.flush();

				// read a line of data from the stream
				String data = in.readLine();
				System.out.println("Received: " + data);
			} while (true);
		} catch (IOException e) {
			System.out.println("IO Exception:" + e.getMessage());
		} finally {
			try {
				if (clientSocket != null) {
					clientSocket.close();
				}
			} catch (IOException e) {
				// ignore exception on close
			}
		}
		System.out.println("Client: bye!");
	}
}