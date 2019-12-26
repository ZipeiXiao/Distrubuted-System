import java.math.BigInteger;
import java.rmi.*;
public interface Bumper extends Remote {
    boolean bump() throws RemoteException;
    BigInteger get() throws RemoteException;
}