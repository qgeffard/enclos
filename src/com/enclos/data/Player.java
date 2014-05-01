package com.enclos.data;

import java.util.ArrayList;
import java.util.List;

import com.enclos.component.Sheep;

public class Player implements PlayerAction {
    public final static int BEGIN_TURN = 0;
    public final static int MOVE_SHEEP = 2;
    public final static int DROP_BARRIER = 1;
    public final static int END_TURN = 3;
    
    protected int turnStatus;
    protected boolean hasLost = false;
    protected List<Sheep> sheeps = new ArrayList<>();

    @Override
    public void moveSheep() {
        this.turnStatus += MOVE_SHEEP;
    }

    @Override
	public void dropBarrier() {
        this.turnStatus += DROP_BARRIER;
    }
   
    public List<Sheep> getSheeps() {
        return this.sheeps;
    }
    
    public boolean hasLost() {
        return this.hasLost;
    }

    public void paralyzed(){
        this.hasLost = true;
    }
    
    public void alive() {
		this.hasLost = false;
	}
	
    public void resetLoseStatus(){
		hasLost = false;
	}
	
    public void startTurn() {
        this.turnStatus = BEGIN_TURN;
    }

    public int getTurnStatus() {
        return turnStatus;
    }
    
    public void setTurnStatus(int turnStatus) {
        this.turnStatus = turnStatus;
    }
    
    public boolean isEndOfTurn() {
        return (this.turnStatus >= END_TURN) ? true : false;
    }
    
    public boolean isBeginOfTurn(){
    	return (this.turnStatus == BEGIN_TURN) ? true : false;
    }


}
