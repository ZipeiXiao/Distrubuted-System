/**
 * Author: Zipei (Tina) Xiao
 * Last Modified: Oct. 16th, 2019
 *
 * This program demonstrates a very simple stand-alone Blockchain.
 * Behind the scenes, there will be a client server interaction
 * using JSON over TCP sockets.
 *
 * The message transfers in this program is formatted by two JSON messages types
 * – a message to encapsulate requests from the client
 * and a message to encapsulate responses from the server.
 * Each JSON request message will include a signature.
 */

package blockchaintask0;

import com.alibaba.fastjson.JSON;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static blockchaintask0.Server.getHash;

/**
 * The EchoServerThreadTCP class includes the Key generator
 * and other security methods,
 * i.e., methods other than the TCP socket transfer.
 */
public class EchoServerThreadTCP extends Thread {
    Socket clientSocket = null;
    BlockChain blockChain;

    /**
     * Constructor of this class
     * New a Thread with the correct socket information
     * @param clientSocket the TCP Socket
     * @param blockChain the blockchain objects that the client asks for
     */
    public EchoServerThreadTCP(Socket clientSocket, BlockChain blockChain) {
        this.clientSocket = clientSocket;
        this.blockChain = blockChain;
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
                ServerResponse serverResponse = new ServerResponse();
                String requestString = in.nextLine();

                //received JSON string
                System.out.println("requestString: " + requestString);

                ClientRequest request = JSON.parseObject(requestString, ClientRequest.class);
                ClientMessage message = request.getMessage();

                // Each request to the server must be signed using the private key.
                BigInteger signed = new BigInteger(request.getSignedHash());

                // The signature must be checked on the server.
                BigInteger clear = signed.modPow(message.getE(), message.getN());

                // Decode to a string
                String clearStr = new String(clear.toByteArray());

                // It is very important that the big integer created with the hash (before signing) is positive
                if (clearStr.equals("00" + getHash(JSON.toJSONString(message)))) {
                    // Sign Verify ok

                    // if the user choose to see the status
                    if (message.getMenu() == '0') {
                        serverResponse.appendln(String.format("Current size of chain: %d", blockChain.getChainSize()));
                        serverResponse.appendln(String.format("Current hashes per second by this machine: %d",
                                blockChain.hashesPerSecond()));
                        serverResponse.appendln(String.format("Difficulty of most recent block: %d",
                                blockChain.getLatestBlock().getDifficulty()));
                        serverResponse.appendln(String.format("Nonce for most recent block: %s",
                                blockChain.getLatestBlock().getNonce().toString()));
                        serverResponse.appendln(String.format("Chain hash: %s", blockChain.getChainHash()));
                    } else if (message.getMenu() == '1') {

                        // All blocks added to the Blockchain will have a difficulty
                        // passed in to the program by the user at run time.
                        long currentTimeMillis = System.currentTimeMillis();

                        //new block
                        Block newBlock = new Block(
                                // increase the index by one
                                blockChain.getLatestBlock().getIndex() + 1,
                                new Timestamp(System.currentTimeMillis()),
                                message.getTransaction(),
                                message.getDifficulty());

                        //This new block's previous hash must hold the hash of the most recently added block
                        newBlock.setPrevHash(blockChain.getChainHash());

                        // update the class field using the hash value of new block
                        String chainHash = newBlock.proofOfWork();
                        blockChain.setChainHash(chainHash);

                        // Add a block containing that transaction to the block chain.
                        blockChain.addBlock(newBlock);
                        serverResponse.appendln(String.format(
                                "Total execution time to add this block was %d milliseconds",
                                System.currentTimeMillis() - currentTimeMillis));

                        // If the user selects option 2
                    } else if (message.getMenu() == '2') {
                        long currentTimeMillis = System.currentTimeMillis();
                        serverResponse.appendln("Verifying entire chain");

                        // call the isChainValid method and display the results.
                        serverResponse.appendln(String.format("Chain verification: %b", blockChain.isChainValid()));

                        // display the number of milliseconds it took for validate to run.
                        serverResponse.appendln(String.format(
                                "Total execution time required to verify the chain was %d milliseconds",
                                System.currentTimeMillis() - currentTimeMillis));
                    } else if (message.getMenu() == '3') {
                        serverResponse.appendln("View the Blockchain");
                        serverResponse.appendln(JSON.toJSONString(blockChain));
                    } else if (message.getMenu() == '4') {
                        serverResponse.appendln("Corrupt the Blockchain");
                        if (message.getBlockID() >= 0 && message.getBlockID() < blockChain.getChainSize()) {
                            List<Block> blocks = blockChain.getDs_chain();

                            // get the block that user want to change
                            Block block = blocks.get(message.getBlockID());

                            // add new data for block
                            block.setTx(message.getTransaction());
                            serverResponse.appendln(String.format(
                                    "Block %d now holds %s",
                                    message.getBlockID(),
                                    block.getTx()));
                        }
                    } else if (message.getMenu() == '5') {
                        serverResponse.appendln("Repairing the entire chain");
                        long currentTimeMillis = System.currentTimeMillis();
                        blockChain.repairChain();
                        serverResponse.appendln(String.format(
                                "Total execution time required to repair the chain was %d milliseconds",
                                System.currentTimeMillis() - currentTimeMillis));
                    }
                } else {
                    // If the signature fails to verify, send an appropriate error message back to the client.
                    serverResponse.setMessageBody("Error in request");
                }
                System.out.println(serverResponse.getMessageBody());
                out.println(JSON.toJSONString(serverResponse));
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
}
