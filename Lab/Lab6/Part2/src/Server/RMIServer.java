package Server;

import java.rmi.Naming;

public class RMIServer {
    public RMIServer(){}

    public static void main(String[] var0) {
        System.out.println("Calculator Server Running");

        try {
            RMIServant var1 = new RMIServant();
            System.out.println("Created Bumper object");
            System.out.println("Placing in registry");
            Naming.rebind("bumper", var1);
            System.out.println("RMIServant object ready");
        } catch (Exception e) {
            System.out.println("RMIServer error main " + e.getMessage());
        }

    }
}

