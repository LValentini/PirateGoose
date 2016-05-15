package pirateGooseClient;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.HashMap;

import pirateGooseServer.ServerInterface;

public class ClientNode {
	
	private HashMap<String, String> clientsTable = new HashMap<String, String>();
	private String clientID;
	private String clientKey;
	private String clientIP;
	private MatchHandlerInterface match;
	
	public ClientNode(String registrationServerAddress) throws RemoteException
	{
		clientKey = String.valueOf((int)(10000 + Math.random() * (99999 - 10000) + 1));
		String rmiServer = "rmi://" + registrationServerAddress + ":1099/RegistrationServer";
		System.out.println("Registration: " + rmiServer);
		try {
			ServerInterface registrationServer = (ServerInterface)Naming.lookup(rmiServer);
			clientsTable = registrationServer.playerRegistration(this.clientKey);
			updateClientInfo(clientsTable, clientKey);
		} catch (Exception e) {
			System.out.println("Registration Error");
		};	
		
		try {
			java.rmi.registry.LocateRegistry.createRegistry((1100+Integer.valueOf(this.clientID)));
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		
		match = new MatchHandler(clientsTable, (int)Integer.valueOf(clientID), clientKey, clientIP);
		
		String rmiService = "rmi://"+clientIP+":"+(1100+Integer.valueOf(this.clientID))+"/pirateGoose"+clientKey;

		System.out.println(rmiService);
		
		try {
			Naming.rebind(rmiService, match);
		} catch (RemoteException e) {
			//e.printStackTrace();
		} catch (MalformedURLException e) {
			//e.printStackTrace();
		}
	}
	
	

	
	public MatchHandlerInterface getMatch() {
		return this.match;
	}




	private void updateClientInfo (HashMap<String, String> clients, String key) {				
			String info = clients.get(key);
			String array[] = info.split("#");
			this.clientID = array[0];
			this.clientIP = array[1];
	}
	

	public void registration() {
	}
	
	

}
	