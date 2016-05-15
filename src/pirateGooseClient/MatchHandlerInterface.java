package pirateGooseClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MatchHandlerInterface extends Remote {
	public void launchMatch(GameView game) throws RemoteException;
	public void removeClient(MatchHandlerInterface deadClient) throws RemoteException;
	public void receiveMessage(int senderID, int receivedToken, int moveValue, boolean gameEnded) throws RemoteException;
	public boolean receivedPing(int clientID) throws RemoteException;
	public void receiveToken(int receivedToken) throws RemoteException;
}
