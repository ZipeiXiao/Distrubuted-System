// CalculatorClient.java                 
// This client gets a remote reference from the rmiregistry
// that is listening on the default port of 1099.
// It allows the client to quit with a "!". 
// Otherwise, it computes the sum of two integers 
// using the remote calculator.

import java.rmi.*;
import java.rmi.server.*;
import java.io.*;
import java.math.BigInteger;
import java.util.StringTokenizer;

public class BumperClient {
   public static void main(String args[]) throws Exception {
        Bumper bump  = (Bumper) Naming.lookup("//localhost/bumper");
 	    System.out.println("Found bumper");
        try {
            BigInteger ctr = BigInteger.ZERO;
            BigInteger n = new BigInteger("10000");
            long start = System.currentTimeMillis();
            while (!ctr.equals(n)) {
                ctr = ctr.add(BigInteger.ONE);
                bump.bump();
            }
            long stop = System.currentTimeMillis();

            System.out.println("current number: " + bump.get());
            System.out.println("time used: " + (stop - start));
            
        } catch(RemoteException e) {
            System.out.println("allComments: " + e.getMessage());
        }
    }
    
}
