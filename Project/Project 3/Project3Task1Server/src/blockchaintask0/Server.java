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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;

/**
 * This class demonstrates a very simple TCP server.
 *
 * The packet is sent to the client asynchronously.
 * When the request packet arrives, a String object is created
 * and the partial sum is displayed.
 * The program illustrates the marshaling and un-marshaling
 * of requests and replies.
 */
public class Server {

    public static String getHash(String inStr) {
        return BabyHash.ComputeSHA_256_as_Hex_String(inStr);
    }

    /**
     * The socket functions of a server
     * @param args
     */
    public static void main(String args[]) {
        System.out.println("Server Running...");
        BlockChain blockChain = new BlockChain();

        try {
            //first block: the so called Genesis block
            Block lastBlock = new Block(0, new Timestamp(System.currentTimeMillis()), "Genesis", 2);

            //This new block's previous hash must hold the hash of the most recently added block
            lastBlock.setPrevHash(blockChain.getChainHash());

            // update the class field using the hash value of new block
            String chainHash = lastBlock.proofOfWork();
            blockChain.setChainHash(chainHash);

            // Add a block containing that transaction to the block chain.
            blockChain.addBlock(lastBlock);

            // the server port we are using
            int serverPort = 7777;

            // Create a new server socket
            ServerSocket listenSocket = new ServerSocket(serverPort);

            /*
             * Block waiting for a new connection request from a client. When the request is
             * received, "accept" it, and the rest the tcp protocol handshake will then take
             * place, making the socket ready for reading and writing.
             */
            while (true) {
                Socket clientSocket = listenSocket.accept();

                // If we get here, then we are now connected to a client.
                EchoServerThreadTCP thread = new EchoServerThreadTCP(clientSocket, blockChain);
                thread.start();
            }

            // Handle exceptions
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());

            // If quitting (typically by you sending quit signal) clean up sockets
        } finally {
        }
    }
}
