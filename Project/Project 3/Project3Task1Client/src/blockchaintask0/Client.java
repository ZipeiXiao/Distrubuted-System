/**
 * Author: Zipei (Tina) Xiao
 * Last Modified: Oct. 16th, 2019
 *
 * This program demonstrates a very simple stand-alone Blockchain.
 * Behind the scenes, there will be a client server interaction
 * using JSON over TCP sockets.
 * Each request to the server must be signed using the
 * private key. The signature must be checked on the
 * server. If the signature fails to verify,
 * send an appropriate error message back to the client.
 *
 * The message transfers in this program is formatted by two JSON messages types
 * – a message to encapsulate requests from the client
 * and a message to encapsulate responses from the server.
 * Each JSON request message will include a signature.
 */

package blockchaintask0;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Random;

/**
 * This Client class demonstrates a very simple TCP client.
 */
public class Client {
    /**
     * The Client class has a field called e - the public key: e and n
     */
    private static BigInteger e;
    /**
     * The Client class has a field called d - the private key: d & n
     */
    private static BigInteger d;
    /**
     * The Client class has a field called n - the public key: e & n or the private key: d & n
     */
    private static BigInteger n;

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

        // d and n is the private key
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

        //System.out.println(" e = " + e.toString()); // Step 6: (e,n) is the RSA public key
        //System.out.println(" d = " + d.toString()); // Step 7: (d,n) is the RSA private key
        //System.out.println(" n = " + n.toString()); // Modulus for both keys
    }

    public static void main(java.lang.String[] args) {
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
            do {

                // the client is interactive and menu driven.
                System.out.println("Main menu:");
                System.out.println("0. View basic blockchain status.");
                System.out.println("1. Add a transaction to the blockchain.");
                System.out.println("2. Verify the blockchain.");
                System.out.println("3. View the blockchain.");
                System.out.println("4. Corrupt the chain.");
                System.out.println("5. Hide the Corruption by repairing the chain.");

                // an option to exit.
                System.out.println("6. Exit");

                String nextLine = typed.readLine();
                ClientMessage clientMessage = new ClientMessage(n, e, nextLine.charAt(0));

                if (nextLine.charAt(0) == '6') {
                    System.out.println("Exit...");
                    break;

                    // 0. View basic blockchain status.
                } else if (nextLine.charAt(0) == '0') {

                    // 1. Add a transaction to the blockchain.
                } else if (nextLine.charAt(0) == '1') {
                    System.out.println("Enter difficulty > 0");
                    int difficulty = Integer.valueOf(typed.readLine());
                    if (difficulty > 0) {
                        System.out.println("Enter transaction");
                        String transaction = typed.readLine();
                        clientMessage.setDifficulty(difficulty);
                        clientMessage.setTransaction(transaction);
                    }

                    // 2. Verify the blockchain.
                } else if (nextLine.charAt(0) == '2') {

                    // 3. View the blockchain.
                } else if (nextLine.charAt(0) == '3') {

                    // 4. Corrupt the chain.
                } else if (nextLine.charAt(0) == '4') {
                    System.out.println("Corrupt the Blockchain");
                    System.out.println("Enter block ID of block to Corrupt");
                    int id = Integer.valueOf(typed.readLine());
                    System.out.println(String.format("Enter new data for block %d", id));
                    String transaction = typed.readLine();
                    clientMessage.setBlockID(id);
                    clientMessage.setTransaction(transaction);

                    //5. Hide the Corruption by repairing the chain.
                } else if (nextLine.charAt(0) == '5') {
                }

                String messageToSigned = JSON.toJSONString(clientMessage);

                // Each request to the server must be signed using the private key
                // the client will sign each request. The signature will be added to each request.
                ClientRequest clientRequest = new ClientRequest(
                        clientMessage,
                        // It is very important that the big integer created with the hash (before
                        // signing) is positive
                        getSignedHashOfMessage("00" + getHash(messageToSigned)));

                //send request JSON string to server
                out.println(JSON.toJSONString(clientRequest));

                //flush
                out.flush();

                // read a line of data from the stream
                String data = in.readLine();

                // JSON messages types – a message to encapsulate requests from the client
                ServerResponse res = JSON.parseObject(data, ServerResponse.class);
                System.out.println(res.getMessageBody());
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
        System.out.println("Client bye!");
    }
}
