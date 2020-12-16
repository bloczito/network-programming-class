import java.rmi.Remote;
import java.rmi.RemoteException;
public interface RMIInterface extends Remote {
    public String helloTo(String name,String surname,String sel,char[] pass) throws RemoteException;
}
