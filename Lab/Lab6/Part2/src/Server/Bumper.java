package Server;

import java.math.BigInteger;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Bumper extends Remote{
    BigInteger bi = new BigInteger("0");
    boolean bump() throws RemoteException;
    BigInteger get() throws RemoteException;
}
