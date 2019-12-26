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

import java.math.BigInteger;

/**
 * This class holds the messages from server to client
 */
public class ClientMessage {
    /**
     * The ClientMessage class has a field called n - client’s public key(n,e)
     */
    private BigInteger n;
    /**
     * The ClientMessage class has a field called e - client’s public key(n,e)
     */
    private BigInteger e;
    /**
     * The ClientMessage class has a field called menu - the menu that user's choose
     */
    private char menu = '0';
    /**
     * The ClientMessage class has a field called blockID - the ID of a block
     */
    private int blockID = 0;
    /**
     * The ClientMessage class has a field called difficulty - the difficulty level of a block
     */
    private int difficulty = 0;
    /**
     * The ClientMessage class has a field called transaction - the block transaction data
     */
    private String transaction = "";

    /**
     * Default Constructor
     */
    public ClientMessage() {
        super();
    }

    /**
     * Constructor to hold the necessary message information
     * @param n the n in the public key
     * @param e the e in the public key
     * @param menu the menu index that client choose
     */
    public ClientMessage(BigInteger n, BigInteger e, char menu) {
        this.n = n;
        this.e = e;
        this.menu = menu;
    }

    /**
     * Get the n of the public key
     * @return the n in the public key
     */
    public BigInteger getN() {
        return n;
    }

    /**
     * Set the n of the public key
     * @param n the n in the public key
     */
    public void setN(BigInteger n) {
        this.n = n;
    }

    /**
     * Get the e of the public key
     * @return the e in the public key
     */
    public BigInteger getE() {
        return e;
    }

    /**
     * Set the e of the public key
     * @param e the e in the public key
     */
    public void setE(BigInteger e) {
        this.e = e;
    }

    /**
     * Get the menu index that client choose
     * @return the menu index that client choose
     */
    public char getMenu() {
        return menu;
    }

    /**
     * Set the menu index that client choose
     * @param menu the menu index that client choose
     */
    public void setMenu(char menu) {
        this.menu = menu;
    }

    /**
     * Get the ID of a block
     * @return the ID of a block
     */
    public int getBlockID() {
        return blockID;
    }

    /**
     * Set the ID of a block
     * @param blockID the ID of a block
     */
    public void setBlockID(int blockID) {
        this.blockID = blockID;
    }

    /**
     * Get the difficulty level of a block
     * @return the difficulty level of a block
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Set the difficulty level of a block
     * @param difficulty the difficulty level of a block
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Get the block transaction data
     * @return the block transaction data
     */
    public String getTransaction() {
        return transaction;
    }

    /**
     * Set the block transaction data
     * @param transaction the block transaction data
     */
    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }
}
