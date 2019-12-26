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
 * The ServerResponse class holds the server response message send to client
 */
public class ServerResponse {

    /**
     * The ServerResponse class has only one field called messageBody - the response message body
     */
    private String messageBody = "";

    /**
     * Default constructor
     */
    public ServerResponse() {
        super();
    }

    /**
     * Constructor that holds necessary information of the message
     * @param messageBody the response message body
     */
    public ServerResponse(String messageBody) {
        this.messageBody = messageBody;
    }

    /**
     * Get the response message body
     *
     * @return the response message body
     */
    public String getMessageBody() {
        return messageBody;
    }

    /**
     * Set the response message body
     *
     * @param messageBody the response message body
     */
    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    /**
     * Method that helps the format of message to client.
     * Like the Println() method
     *
     * @param messageBody the response message body
     */
    public void appendln(String messageBody) {
        this.messageBody += messageBody + "\n";
    }
}
