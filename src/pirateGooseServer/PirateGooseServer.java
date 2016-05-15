package pirateGooseServer;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import pirateGooseServer.ServerInterface;


public class PirateGooseServer extends UnicastRemoteObject implements ServerInterface {

	private static final long serialVersionUID = 1L;
	private int maxPlayers = 4;
	private int registeredPlayers;
	private String clientInfo;
	private HashMap<String, String> playersTable = new HashMap<String, String>();
	private Boolean roomFull = false;
	
	protected PirateGooseServer() throws RemoteException {
		super();
		this.registeredPlayers = 0;
	}
	
	
	
	public HashMap<String, String> playerRegistration(String clientKey) throws RemoteException, InterruptedException {
		if (roomFull == true) {
			resetRoom();
		}
		if (registeredPlayers <= maxPlayers-1) {
			try {
					String playerIP = RemoteServer.getClientHost();
					clientInfo = new String(registeredPlayers+"#"+playerIP);
					playersTable.put(clientKey, clientInfo);
					registeredPlayers++;
					System.out.println("Client " + clientInfo + " connected");
			} 
			catch (ServerNotActiveException e) {
					e.printStackTrace();
			}
			while (registeredPlayers != maxPlayers) { Thread.sleep(1000); };
			roomFull = true;
			return playersTable;
		}
		else {
			System.out.println("Too many players");
			return null;
		}	
	} 
	
	private void resetRoom() {
		this.playersTable = new HashMap<String, String>();
		this.clientInfo = null;
		this.registeredPlayers = 0;
		roomFull = false;
	}
	
	public static void main(String[] args) throws Exception{
			  LocateRegistry.createRegistry(1099);
			  ServerInterface server = new PirateGooseServer();
			  String objectURL = "rmi://127.0.0.1:1099/RegistrationServer";
			  Naming.rebind(objectURL, server);
			  System.out.println("Registration server flying");
		  }

}
