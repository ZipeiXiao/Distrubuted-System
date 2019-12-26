package Server;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIServant extends UnicastRemoteObject implements Bumper {
    private static BigInteger bi;

    public RMIServant() throws RemoteException {
    }
    public boolean bump() throws RemoteException {
        // A call on bump() adds 1 to a BigInteger held by the service.
        System.out.println("Got request to add 1");
        bi = bi.add(new BigInteger("1"));

        // It then returns true on completion.
        return true;

        // The BigInteger is changed by the call on bump(). That is,
        // 1 is added to the BigInteger and that value persists until
        // another call on bump occurs.
    }

    public BigInteger get() throws RemoteException {
        // a call on get returns the BigInteger held by the service
        return bi;
    }
}
