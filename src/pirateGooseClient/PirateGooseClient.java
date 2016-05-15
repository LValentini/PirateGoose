package pirateGooseClient;

import java.rmi.RemoteException;

public class PirateGooseClient {
	
	private static String registrationServerAddress;
	private GameView gameView;
	private ClientNode clientNode;
	private MatchHandlerInterface matchHandler;
	public PirateGooseClient() throws RemoteException {
		gameView = new GameView();
		gameView.setVisible(true);
		try {
			clientNode = new ClientNode(registrationServerAddress);
		} catch (RemoteException e) {
			System.out.println("Registration error");
		}
		try {
			matchHandler = clientNode.getMatch();
			matchHandler.launchMatch(gameView);
		} catch (RemoteException e) {
			System.out.println("Error launching the game");
		}
		
	}
	
	public static void main(String [] args) throws RemoteException
	{
		registrationServerAddress = args[0];
		new PirateGooseClient();
	}
	
}
