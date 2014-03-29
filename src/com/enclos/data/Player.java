package com.enclos.data;

import java.awt.Image;
import java.util.Map;

import javax.swing.ImageIcon;

public class Player implements PlayerAction{
	public final static int BEGIN_TURN = 0;
	public final static int MOVE_SHEEP = 2;
	public final static int DROP_BARRIER = 1;
	public final static int END_TURN = 3;
	private String firstName;
	private String lastName;
	private int age;
	private int turnStatus;
	
	
	//path par defaut ?
	private Image profilePicture = null;
	// score correspondant au nom d'une partie
	//private Map<String, Integer> score;
	
	public Player(String firstName, String lastName, int age){
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
	}
	
	public Player(String firstName, String lastName, int age, String picturePath){
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.profilePicture = new ImageIcon(picturePath).getImage();
	}
	
	public void startTurn(){
		this.turnStatus = BEGIN_TURN;
	}
	
	public boolean isEndOfTurn(){
		return (this.turnStatus >= END_TURN) ? true : false;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public int getAge() {
		return age;
	}

	public Image getProfilePicture() {
		return profilePicture;
	}

	public int getTurnStatus() {
		return turnStatus;
	}

	public void setTurnStatus(int turnStatus) {
		this.turnStatus = turnStatus;
	}

	@Override
	public String toString(){
		StringBuilder description = new StringBuilder();
		description.append("Firstname : ");
		description.append(this.firstName);
		description.append("\n");
		description.append("Lastname : ");
		description.append(this.lastName);
		description.append("\n");
		description.append("Age : ");
		description.append(this.age);
		description.append("\n");
		description.append("Profile Pic : ");
		description.append(this.profilePicture);
		description.append("\n");
		return description.toString();
	}

	@Override
	public void moveSheep() {
		this.turnStatus += MOVE_SHEEP;
		
	}

	@Override
	public void dropBarrier() {
		System.out.println(this.firstName+" "+this.lastName+" a posé une barriere");
		this.turnStatus += DROP_BARRIER;
	}
	
	
	
}
