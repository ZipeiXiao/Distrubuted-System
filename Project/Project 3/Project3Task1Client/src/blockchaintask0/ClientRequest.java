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

/**
 * This class holds the request message sent by client to the server
 */
public class ClientRequest {
    /**
     * The ClientRequest class has a field called message - user message
     */
    private ClientMessage message;
    /**
     * The ClientRequest class has a field called signedHash - signed hash of message
     */
    private String signedHash;

    /**
     * Default constructor
     */
    public ClientRequest() {
        super();
    }

    /**
     * Constructor really used to hold necessary information from client to the server
     * @param message user message
     * @param signedHash signed hash of message
     */
    public ClientRequest(ClientMessage message, String signedHash) {
        this.message = message;
        this.signedHash = signedHash;
    }

    /**
     * Get user message
     * @return user message
     */
    public ClientMessage getMessage() {
        return message;
    }

    /**
     * Set the user message
     * @param message user message
     */
    public void setMessage(ClientMessage message) {
        this.message = message;
    }

    /**
     * Get signed hash of message
     * @return signed hash of message
     */
    public String getSignedHash() {
        return signedHash;
    }

    /**
     * Set signed hash of message
     * @param signedHash signed hash of message
     */
    public void setSignedHash(String signedHash) {
        this.signedHash = signedHash;
    }
}
