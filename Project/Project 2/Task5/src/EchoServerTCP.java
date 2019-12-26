/**
 * Author: Zipei (Tina) Xiao
 * Last Modified: Oct. 3rd, 2019
 *
 * This program demonstrates a very simple TCP server.
 *
 * The packet is sent to the client asynchronously.
 * When the request packet arrives, a String object is created
 * and the partial sum is displayed.
 * The program illustrates the marshaling and un-marshaling
 * of requests and replies.
 */

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class EchoServerTCP {

	/**
	 * The socket functions of a server
	 * @param args
	 */
	public static void main(String args[]) {
		System.out.println("Server Running...");
		try {

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
				EchoServerThreadTCP thread = new EchoServerThreadTCP(clientSocket);
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
