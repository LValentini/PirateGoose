package pirateGooseClient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;

import ch.aplu.jgamegrid.GameGrid;

public class MatchHandler extends UnicastRemoteObject implements MatchHandlerInterface {
	
	private static final long serialVersionUID = 1L;
	private HashMap<String, String> clientsInfo;
	private int clientID;
	private ClientHandler[] players = new ClientHandler[4];
	private LinkedList<ClientHandler> playersList= new LinkedList<ClientHandler>();
	private int nextPlayer = 0;
	private LinkedList<MatchHandlerInterface> playersServices = new LinkedList<MatchHandlerInterface>();
	private int token = 0;
	private boolean gameEnded = false;
	private GameView game;
	private int dice;

	public MatchHandler(HashMap<String, String> clientsInfo, int clientID, String clientKey, String clientIP) throws RemoteException {
		this.setClientsInfo(clientsInfo);
		this.clientID = clientID;
		this.players = createPlayersArray(clientsInfo);
	}

	public ClientHandler[] getPlayers() {
		return players;
	}

	private ClientHandler[] createPlayersArray(HashMap<String, String> clients) {
		for (String key : clients.keySet()) {		
			String info = clients.get(key);
			String array[] = info.split("#");
			Integer id = Integer.valueOf(array[0]);
			String ip = array[1];
			players[id]	 = new ClientHandler(key, ip, id);
		}
		return players;
	}
	
	

	public void launchMatch(GameView game) throws RemoteException {
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		this.game = game;
		this.game.setPlayerID(this.clientID);
		this.nextPlayer = (clientID+1)%4;
		try {
			this.playersServices.add((MatchHandlerInterface)Naming.lookup(this.players[nextPlayer].getRmiURI()));
			this.playersList.add(this.players[nextPlayer]);		
		} catch (RemoteException e) {
			
		} catch (MalformedURLException e) {
			
		} catch (NotBoundException e) {
		
		}

		
		
		try {
			this.playersServices.add((MatchHandlerInterface)Naming.lookup(this.players[(nextPlayer+1)%4].getRmiURI()));	
			this.playersList.add(this.players[(nextPlayer+1)%4]);
		} catch (RemoteException e1) {
			
		} catch (MalformedURLException e1) {
			
		} catch (NotBoundException e1) {
			
		}

		try {
			this.playersServices.add((MatchHandlerInterface)Naming.lookup(this.players[(nextPlayer+2)%4].getRmiURI()));
			this.playersList.add(this.players[(nextPlayer+2)%4]);
		} catch (RemoteException e2) {
			
		} catch (MalformedURLException e2) {
			
		} catch (NotBoundException e2) {
			
		}
		
		System.out.println(this.clientID + ") Size: " + this.playersList.size());
		System.out.println(this.clientID + ") LastID: " + this.playersList.getLast().getId());
		if (this.playersList.size() == 0)
			this.token = this.clientID;
		else {
			if (this.clientID == 1 && this.playersList.getLast().getId() > 1)
				this.token = 1;
			if (this.clientID == 2 && this.playersList.getLast().getId() > 2)
				this.token = 2;
		}
		System.out.println(this.clientID + ") Token: " + this.token);
		
		//game.setVisible(true);

		
		while (true) {
			// Cycle
				// My turn, broadcasts the move and the token and set lastPlayer as the next one
				if (this.token == this.clientID) {
					this.game.diceUp(true);
					this.dice = this.game.waitForDice();
					try {
						Thread.sleep(1000);
						GameGrid.delay(30);
						this.game.movePlayer(this.clientID, this.dice);
						this.gameEnded = this.game.gameEnded();
					} 
					catch (InterruptedException seee) {
					}
					if (this.playersServices.size() >= 1) {
						for (int i = 0; i < this.playersServices.size(); i++) {			
							try {
								this.playersServices.get(i).receiveMessage(this.clientID, this.playersList.get(0).getId(), this.dice, this.gameEnded);
							} 
							catch (RemoteException e) {	
							}	
						}
						this.token = this.playersList.get(0).getId();
						this.game.diceUp(false);
					}
					if (this.gameEnded)
						this.game.setWinner(this.clientID);
					}
				else
					try {
						waitForToken();
					} 
					catch (InterruptedException e3) {
					}
				// try to ping the previous player in the ring, if it's down remove it from the structures and broadcasts the removeClient method
				if (this.playersServices.size() >= 1) {
					System.out.println("PING!");
				try {
					this.playersServices.getLast().receivedPing(this.clientID);
				} catch (RemoteException e) {
					System.out.println(this.clientID + " - PING Failed");
					for (int i = 0; i < this.playersServices.size() ; i++) {
						try {
							this.playersServices.get(i).removeClient(this.playersServices.getLast());
							this.game.removePlayer(this.playersList.getLast().getId());
						} catch (RemoteException e1) {
						}	 
					}
					this.playersServices.removeLast();
					if (this.playersServices.size() == 0)
							this.game.setWinner(this.clientID);
				}
				}
				
				
		}
		
					
	}
	
	
	public void removeClient(MatchHandlerInterface deadClient) throws RemoteException {
		int deadPlayer = this.playersServices.indexOf(deadClient);
		int deadPlayerID = this.playersList.get(this.playersServices.indexOf(deadClient)).getId();
		// if the dead one was the one with the token, the next client receive it
		if (this.playersList.size() >= 1) {
		if (deadPlayerID == this.token) {			
			this.token = this.playersList.get((deadPlayer+1)%playersServices.size()).getId();

			System.out.println("New token should be: " + this.token);
			for (int i = 0; i < playersServices.size(); i++) {
				try {
					this.playersServices.get(i).receiveToken(this.token);
				} catch (RemoteException e) {

				}
			}
		}
		} else { 
			this.token = this.clientID;
			this.game.setWinner(this.clientID);
			}
		this.playersServices.remove(deadClient);
		this.playersList.remove(deadPlayer);
		this.game.removePlayer(deadPlayerID);
		System.out.println("Remaining clients: " + (this.playersServices.size()+1));
	}
	
	
	public boolean receivedPing(int clientID) throws RemoteException {
		return true;
	}
	
	public void waitForToken() throws InterruptedException {
			Thread.sleep(3000);
	}

	public void receiveMessage(int senderID, int receivedToken, int moveValue, boolean gameEnded) throws RemoteException {		
		this.game.movePlayer(senderID, moveValue);
		if (!gameEnded) {
			this.token = receivedToken;
			if (this.token == this.clientID) {
				this.game.diceUp(true);
			}
		} else this.game.setWinner(senderID);
	}
	
	public void receiveToken(int receivedToken) throws RemoteException {
		this.token = receivedToken;
	}

	public HashMap<String, String> getClientsInfo() {
		return clientsInfo;
	}

	public void setClientsInfo(HashMap<String, String> clientsInfo) {
		this.clientsInfo = clientsInfo;
	}
	
}
