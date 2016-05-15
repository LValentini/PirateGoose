package pirateGooseClient;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class Player extends Actor {
	
	private GameBoard board = new GameBoard();
	public void setGgPanel(GameGrid ggPanel) {
		this.ggPanel = ggPanel;
	}

	private boolean setImage = false;
	private Actor cellImage;
	private GameGrid ggPanel;
	
	public void setCellImage(int cellValue) {
		this.ggPanel.removeActorsAt(new Location(150, 423));
		this.cellImage = new Actor("sprites/cellSprites/cell" + cellValue + ".png");
		this.ggPanel.addActor(this.cellImage, new Location(150, 423));
		
	}
	
	  public Player(int playerID)
	  {
		super("sprites/playerIcons/pirate"+playerID+".png");
		board.createGrid(); 
	  }
	
	  public void act(int move, GameGrid grid)
	  {
		moveForward(move, grid);
	    int cellResult = this.board.getCellValue(getX(), getY());
	    while (cellResult != 0) {
	    	if (cellResult > 0) {
	    		GameGrid.delay(1000);
	    		cellResult = moveForward(cellResult, grid);
	    	}
	    	else {
	    		GameGrid.delay(1000);
	    		cellResult = moveBackward(cellResult, grid);
	    	}
	    }
	  }
	  
	  
	    
	 private int moveForward (int move, GameGrid grid) {
		 for (int i = 0; i < move; i++) {
			 	if (getX() == 9 && getY() == 0) {
					 if (this.setImage) {
						 setCellImage(100);
					 }
			 		return 0;
			 		//break;
			 	}
	    		if (getX() == 9) {
	    			turn(270);
	    			move(1);
	    		}
	    		else 
	    			if (getX() == 0 && getY() != 8) {
	    				turn(90);
	    				move(1);
  				} 
	    			else
	    				move(1);
	    		GameGrid.delay(50);
	    		grid.refresh();
		 }
		 if (this.setImage) {
			 setCellImage(this.board.getCellValue(getX(), getY()));
		 }
		 return this.board.getCellValue(getX(), getY());
	 }
		 
	 
	 public void setSetImage(boolean setImage) {
		this.setImage = setImage;
	}

	private int moveBackward (int move, GameGrid grid) { 
		 for (int i = 0; i > move; i--) {
	    		if (getX() == 9) {
	    			if (getY()%2 == 0) {
	    				turn(180);
	    				move(1);
	    				turn(180);
	    			}
	    			else {
	    		
	    				turn(180);
	    				move(1);
	    				turn(270);
	    			}
	    		}
	    		else { 
	    			if (getX() == 0) {
	    				if (getY()%2 == 1) {
	    	
	    					turn(180);
	    					move(1);
	    					turn(180);
	    				}
	    				else {
	    		
	    					turn(180);
	    					move(1);
	    					turn(90);
	    				}
	    			}
	    			else {
	    			
	    				turn(180);
	    				move(1);
	    				turn(180);
	    				if (getX() == 9 && getY()%2 == 1) {
	    					turn(90);
	    				}
	    				if (getX() == 0 && getY()%2 == 0) {
	    					turn(270);
	    				}
	    			}
	    		}
	    		if (getX() == 0 && getY() == 8) {
	    			turn(90);
	    		}
	    		GameGrid.delay(50);
	    		grid.refresh();	
		 }
	
		 if (this.setImage) {
			 setCellImage(this.board.getCellValue(getX(), getY()));
		 }
		 return this.board.getCellValue(getX(), getY());
	 }

}
