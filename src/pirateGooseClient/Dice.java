package pirateGooseClient;

import ch.aplu.jgamegrid.Actor;

public class Dice extends Actor {
	public Dice()
	{
		super(true, "sprites/dices/dice.png", 7);
	}
	
	public void act() {
		showNextSprite();
	}
	
	public int rollDice() {
		int result = (int)(1 + Math.random() * (7 - 1));
		//System.out.println("Result: " + result);
		return result;
	}
}

