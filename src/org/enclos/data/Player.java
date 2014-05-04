package org.enclos.data;

import java.util.ArrayList;
import java.util.List;

import org.enclos.component.Sheep;

/**
 * @author Clement CARREAU
 * @author Quentin GEFFARD
 * @author Julien TELA
 */

public class Player implements PlayerAction {
	public final static int BEGIN_TURN = 0;
	public final static int MOVE_SHEEP = 2;
	public final static int DROP_BARRIER = 1;
	public final static int END_TURN = 3;

	protected String firstName;
	protected String lastName;

	protected int turnStatus;
	protected boolean hasLost = false;
	protected List<Sheep> sheeps = new ArrayList<>();

	/**
	 * Getter first name of the player instance
	 * @return String
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Getter last name of the player instance
	 * @return String
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Update the player turn status attribute with the value corresponding of the Move_sheep constant 
	 */
	public void moveSheep() {
		this.turnStatus += MOVE_SHEEP;
	}
	
	/**
	 * Update the player turn status attribute with the value corresponding of the Drop_barrier constant 
	 */
	public void dropBarrier() {
		this.turnStatus += DROP_BARRIER;
	}
	
	/**
	 * Getter sheeps attribute
	 * @return list of Sheep
	 */
	public List<Sheep> getSheeps() {
		return this.sheeps;
	}
	
	/**
	 * Getter hasLost attribute
	 * @return whether the player has lost
	 */
	public boolean hasLost() {
		return this.hasLost;
	}
	
	/**
	 * Set hasLost attribute at true, used when user can't move any sheep
	 */
	public void paralyzed() {
		this.hasLost = true;
	}
	
	/**
	 * Set hesLost attribute at false, call when player can move again after paralyzed period or on a reinitialized game
	 */
	public void alive() {
		this.hasLost = false;
	}
	
	/**
	 * Update the player turn status attribute with the value corresponding of the BEGIN_TURN constant 
	 */
	public void startTurn() {
		this.turnStatus = BEGIN_TURN;
	}
	
	/**
	 * Getter turnStatus attribute
	 * @return int turn status of the player
	 */
	public int getTurnStatus() {
		return turnStatus;
	}

	/**
	 * Setter turnStatus attribute
	 * @param turnStatus
	 */
	public void setTurnStatus(int turnStatus) {
		this.turnStatus = turnStatus;
	}
	
	/**
	 * Used to check if player turn status is set to end_turn constant
	 * @return boolean true if player move sheep and drop barrier 
	 */
	public boolean isEndOfTurn() {
		return (this.turnStatus >= END_TURN) ? true : false;
	}
	
	/**
	 * Used to check if player turn status is set to BEGIN_TURN constant
	 * @return boolean true if player hasn't done anything yet
	 */
	public boolean isBeginOfTurn() {
		return (this.turnStatus == BEGIN_TURN) ? true : false;
	}

}
