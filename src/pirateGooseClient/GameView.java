package pirateGooseClient;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGButton;
import ch.aplu.jgamegrid.GGButtonListener;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import ch.aplu.util.Monitor;

public class GameView extends JFrame implements GGButtonListener {

	private static final long serialVersionUID = 1L;
	private GameGrid ggGrid;
	private GameGrid ggPanel;
	private Player[] player = new Player[4];
	private Actor diceButtonDisabled = new Actor("sprites/dices/diceButton.png");
	private GGButton diceButtonEnabled = new GGButton("sprites/dices/diceButtonEnabled.png");
	private boolean diceStatus = false;
	private int playerID;
	private int result = 0;
	private boolean gameEnded = false;
	private Dice dice = new Dice();
	private Actor playerImage;
	public boolean ready = true;
	private int winner = 5;

	public int getDice() {
		return result;
	}
	

	public void setWinner(int winner) {
		this.winner = winner;
		this.gameEnded = true;
		if (this.playerID == this.winner+1) {
			this.ggGrid.removeAllActors();
			this.ggGrid.setBgImagePath("sprites/winner.png");
		}
		else {
			this.ggGrid.removeAllActors();
			this.ggGrid.setBgImagePath("sprites/loser.png");
		}
		this.ggGrid.refresh();
	}
	
	public boolean gameEnded() {
		return gameEnded;
	}



	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public void setCellImage(Actor cellImage) {
	}

	public GameView()
	  {
		
		setTitle("Pirate Goose");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		this.ggGrid = new GameGrid(10, 9, 80);
		this.ggGrid.setBgImagePath("sprites/waiting.png");
		getContentPane().add(ggGrid, BorderLayout.EAST);
		
		this.ggPanel = new GameGrid(300, 720, 1);
		this.ggPanel.setBgImagePath("sprites/statusbar.png");
		getContentPane().add(ggPanel, BorderLayout.WEST);
		
		pack();
		
		this.ggPanel.addActor(this.dice, new Location(150, 639));
		this.ggPanel.addActor(diceButtonDisabled, new Location(150, 558));
		this.diceButtonEnabled.addButtonListener(this);
		this.diceButtonEnabled.setEnabled(false);

		
		for (int i = 1; i <= 4; i++) {
			this.player[i-1] = new Player(i);
			this.player[i-1].setGgPanel(this.ggPanel);
			this.ggGrid.addActor(player[i-1], new Location(-1, 8));
		}
		
	    
	  }
	
	public void movePlayer(int playerID, int move) {
		this.setReady(false);
		if (playerID+1 == this.playerID) {
			this.player[playerID].setSetImage(true);
		}
		else this.player[playerID].setSetImage(false);
		this.player[playerID].act(move, ggGrid);
		this.ggGrid.refresh();
		if (this.player[playerID].getX() == 9 && this.player[playerID].getY() == 0) 
		{
			this.setWinner(playerID);
		}
		this.setReady(true);
		
	}
	
	@Override
	public void buttonClicked(GGButton arg0) {	
		for (int i = 0; i < 20; i++) {
			this.dice.show(i%6+1);
			this.ggPanel.refresh();
			try {
				Thread.sleep(100);
				} catch (InterruptedException e) {
				} 
		}
		
		this.result = dice.rollDice();
		this.dice.show(this.result);
		this.ggPanel.removeActorsAt(new Location(150, 558));
		this.ggPanel.addActor(diceButtonDisabled, new Location(150, 558));
		this.ggPanel.refresh();
		Monitor.wakeUp();	
	}

	public void diceUp(boolean status) {
		//if (!gameEnded) {
		this.diceStatus = status;
		if (status) {
			this.ggPanel.removeActorsAt(new Location(150, 558));
			this.ggPanel.addActor(diceButtonEnabled, new Location(150, 558));
		}
		else {
			this.ggPanel.removeActorsAt(new Location(150, 558));
			this.ggPanel.addActor(diceButtonDisabled, new Location(150, 558));
		}
			 
		this.diceButtonEnabled.setEnabled(status);
		this.ggPanel.refresh();
	//	}
		
	}
	
	public boolean isDiceUp() {
		return diceStatus;
	}

	@Override
	public void buttonPressed(GGButton arg0) {
	}

	@Override
	public void buttonReleased(GGButton arg0) {
	}
	
	public void setPlayerID(int playerID) {
		this.playerID = playerID+1;
		this.playerImage = new Actor("sprites/playerAvatars/pirate" + this.playerID + "_id.png");
		this.ggGrid.setBgImagePath("sprites/waterBoard.png");
		this.ggPanel.addActor(playerImage, new Location(140, 207));
		this.ggPanel.refresh();
		this.ggGrid.refresh();
	}
	
	public void setCellImage(int cellValue) {
		new Actor("sprites/cellSprites/cell" + cellValue + ".png");
	}
	
	public int waitForDice() {
		Monitor.putSleep();
		return this.result;
	}
	
	public void removePlayer(int deadClient) {
		System.out.println("Removing player: " + (deadClient+1));
		this.player[deadClient].removeSelf();
		this.ggGrid.refresh();
	}
}
