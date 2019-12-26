package Client;

import java.math.BigInteger;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 *     The main routine loops until the ctr equals n. Each time through the loop,
 *     ctr is incremented by 1. Each time through the loop, the remote  method
 *     bump is called.
 */
public class RMIClient {
    public RMIClient(){}

    public static void main(String[] var0) throws Exception {
        // The main routine performs a lookup on the rmi registry for a remote
        // object that implements the Bumper interface. The object is called
        // "bumper".
        Bumper bumper = (Bumper) Naming.lookup("//localhost/bumper");
        System.out.println("Found bumper.");

        // The main routine creates a BigInteger (called ctr) initialized to 0.
        BigInteger ctr = new BigInteger("0");

        // The main routine creates another BigInteger (called n) initialized to 10000.
        BigInteger n = new BigInteger("10000");

        //Before the loop in the main routine begins, set a timer:
        long start = System.currentTimeMillis();
        while(true) {
            while(ctr.compareTo(n) < 0) {
                try {
                    System.out.print("client>");

                        if(bumper.bump())
                            ctr = bumper.get();
                        System.out.println(ctr);

                } catch (RemoteException e) {
                    System.out.println("allComments: " + e.getMessage());
                }
            }

            // When the loop is finished, find the ending time:
            long stop = System.currentTimeMillis();

            // At the end of the main routine, display the value of the BigInteger held on
            // the server by calling the remote method get().
            System.out.print("the BigInteger held on the server: ");
            System.out.println(bumper.get());

            // In addition, display the number of seconds that it took to call this service 10,000 times.
            System.out.printf("%d seconds", stop - start);
            System.exit(0);
        }
    }
}
