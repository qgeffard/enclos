package com.enclos.data;

public class Computer extends PlayerBase implements PlayerAction {

	private Difficulty difficulty = Difficulty.RETARD;
	
	public Computer(){
		
	}
	
	@Override
    public void startTurn() {
		System.out.println("computer turn end");
        this.turnStatus = END_TURN;
    }
	
	private enum Difficulty {
        RETARD, NORMAL, HELL;

        @Override
        public String toString() {
            // only capitalize the first letter
            String s = super.toString();
            return s.substring(0, 1) + s.substring(1).toLowerCase();
        }
    }

}
