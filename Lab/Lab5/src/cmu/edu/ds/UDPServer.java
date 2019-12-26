package cmu.edu.ds;

import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

/*
Based on Coulouris UDP socket code
 */
public class UDPServer {
    private DatagramSocket socket = null;
    private InetAddress inetAddress = null;
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
        UDPServer udpServer = new UDPServer();
        udpServer.init(7272);

        System.out.println("Bob choose a secret random integer b between 2 and 22:");
        BigInteger b = new BigInteger(2046, new Random());
        System.out.println("b = " + b);

        BigInteger gVaMp = new BigInteger(udpServer.receive());
        System.out.println("Bob received g^a mod p from Alice:");
        System.out.println("g^a % p = " + gVaMp);

        System.out.println("Bob replies with g^b mod p:");
        BigInteger gVbMp = g.modPow(b, modulusP);
        System.out.println("g^b % p = " + gVbMp);
        String message = gVbMp.toString();
        udpServer.send(message);

        BigInteger s = gVaMp.modPow(b, modulusP);
        System.out.println("Bob computes s (the shared secret):" + s);

        udpServer.close();
    }

    private void init(int portnumber) {
        try {
            socket = new DatagramSocket(portnumber);
            System.out.println("Server socket created");
        } catch (SocketException e) {
            System.out.println("Socket error " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO error " + e.getMessage());
        }
    }

    private void send(String message) {
        byte[] buffer = new byte[2046];
        buffer = message.getBytes();
        DatagramPacket reply = new DatagramPacket(buffer, buffer.length, inetAddress, port);
        try {
            socket.send(reply);
        } catch (SocketException e) {
            System.out.println("Socket error " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO error " + e.getMessage());
        }

    }

    private String receive() {
        byte[] buffer = new byte[2046];
        DatagramPacket request = new DatagramPacket(buffer, buffer.length);

        try {
            socket.receive(request);
            inetAddress = request.getAddress();
            port = request.getPort();
        } catch (SocketException e) {
            System.out.println("Socket error " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO error " + e.getMessage());
        }
        return new String(request.getData(), 0, request.getLength());
    }

    private void close() {
        if (socket != null) socket.close();
    }
}