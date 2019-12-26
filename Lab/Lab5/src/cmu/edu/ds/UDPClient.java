package cmu.edu.ds;

import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;
import java.util.Scanner;

/*
Based on Coulouris UDP socket code
 */
public class UDPClient {
    private DatagramSocket socket = null;
    private InetAddress host = null;
    private int port;
    public static BigInteger modulusP = new BigInteger("29455831888140518076474747925200735831996087523515089351" +
            "30571004959603352623816397323936243829918771486116405945830653796692318912148330938019381239117632437182" +
            "14043283060093720669049649181956712189051916260382176617240174711734510352477962712574583690779486253846" +
            "52200912648231914498423025647630580939224343513672606007162748159635064224151355895492579269319645649832" +
            "60578464939552555683472808938112720955867835773494451310665610966359083133030895264190525087963473913134" +
            "73326110069433039169945763380273958809155750154147725521635748917952339066093424140296680685333565455781" +
            "07870365635398276428848740477292742280559");
    public static BigInteger g = new BigInteger("5");

    public static void main(String[] args) {
//        Scanner keyboard = new Scanner(System.in);
        System.out.println("Alice chooses a secret random big integer a that are 2,046 bits in length:");
//        int value = Integer.parseInt(keyboard.nextLine());
//        int a = 2 + (int)(20 * Math.random());
        BigInteger a = new BigInteger(2046, new Random());
        System.out.println("a = " + a);

        System.out.println("Alice transmit g^a mod p to Bob:");
        BigInteger gVaMp = g.modPow(a, modulusP);
        System.out.println("g^a % p = " + gVaMp);

        UDPClient udpClient = new UDPClient();
        udpClient.init("localhost", 7272);
        udpClient.send(gVaMp.toString());

        BigInteger gVbMp = new BigInteger(udpClient.receive());
        System.out.println("Alice receives g^b % p from Bob: " + gVbMp);
        BigInteger s = gVbMp.modPow(a, modulusP);
        System.out.println("Alice computes s (the shared secret):" + s);
        udpClient.close();
    }

    private void init(String hostname, int portNumber) {
        try {
            host = InetAddress.getByName(hostname);
            port = portNumber;
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Socket error " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO error " + e.getMessage());
        }
    }

    private void send(String message) {
        byte[] m = message.getBytes();
        DatagramPacket packet = new DatagramPacket(m, m.length, host, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            System.out.println("IO error " + e.getMessage());
        }
    }

    private String receive() {
        byte[] answer = new byte[2046];
        DatagramPacket reply = new DatagramPacket(answer, answer.length);
        try {
            socket.receive(reply);
        } catch (IOException e) {
            System.out.println("IO error " + e.getMessage());
        }
        return(new String(reply.getData(), 0, reply.getLength()));

    }

    private void close() {
        if (socket != null) socket.close();
    }
}