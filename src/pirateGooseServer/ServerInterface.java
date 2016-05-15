package pirateGooseServer;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface ServerInterface extends Remote {
	
	public HashMap<String, String>  playerRegistration(String clientKey) throws RemoteException, InterruptedException;
}
