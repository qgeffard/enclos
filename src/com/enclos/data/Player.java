package com.enclos.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.enclos.component.Sheep;
import com.enclos.ui.Board;

public class Player implements PlayerAction, Cloneable {
    public final static int BEGIN_TURN = 0;
    public final static int MOVE_SHEEP = 2;
    public final static int DROP_BARRIER = 1;
    public final static int END_TURN = 3;
    private final String firstName;
    private final String lastName;
    private final int age;
    private int gamesWon = 0;
    private int gamesLost = 0;
    private int turnStatus;
    private boolean hasLost = false;
    private List<Sheep> sheeps;

    // only used when saving player
    private String profilePicturePath;

    private BufferedImage profilePicture = null;

    public Player(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.sheeps = new ArrayList<>();
        
        try {
			this.profilePicture = ImageIO.read(new File("resources/image/default_avatar.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public Player(String firstName, String lastName, int age, String picturePath) {
        this(firstName, lastName, age);
        this.profilePicturePath = picturePath;
        try {
            this.profilePicture = ImageIO.read(new File(picturePath));
        } catch (IOException e) {
            try {
                this.profilePicture = ImageIO.read(new File("resources/image/default_avatar.png"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println(firstName + lastName + " : default picture");
        }
    }

    public Player(String firstName, String lastName, int age, int gamesWon, int gamesLost, String picturePath) {
    	this(firstName,lastName,age,picturePath);
    	this.gamesWon = gamesWon;
    	this.gamesLost = gamesLost;
	}

	public void startTurn() {
        this.turnStatus = BEGIN_TURN;
    }

    public boolean isEndOfTurn() {
        return (this.turnStatus >= END_TURN) ? true : false;
    }
    
    public boolean isBeginOfTurn(){
    	return (this.turnStatus == BEGIN_TURN) ? true : false;
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

    public BufferedImage getProfilePicture() {
        return profilePicture;
    }

    public int getTurnStatus() {
        return turnStatus;
    }

    public void setTurnStatus(int turnStatus) {
        this.turnStatus = turnStatus;
    }

    @Override
    public String toString() {
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
        System.out.println(this.firstName + " " + this.lastName + " a posé une barriere");
        this.turnStatus += DROP_BARRIER;
    }

    public List<Sheep> getSheeps() {
        return this.sheeps;
    }

    public String getProfilePicturePath() {
        return this.profilePicturePath;
    }
    
    public int getNumberOfGamesWon(){
    	return this.gamesWon;
    }
    
    public int getNumberOfGamesLost(){
    	return this.gamesLost;
    }
    
    public void win(){
    	this.gamesWon++;
    }

    public boolean hasLost() {
        return this.hasLost;
    }

    public void paralyzed(){
        this.hasLost = true;
    }
    
    public void lose() {
        gamesLost++;
    }

	public void alive() {
		this.hasLost = false;
	}

	private void deepCopySheepList(){
		this.sheeps = new ArrayList<Sheep>();
	}
	
	
	public Player clone() {
		Player clone = null;
		try {
			clone = (Player) super.clone();
			clone.deepCopySheepList();
		} catch(CloneNotSupportedException cnse) {
			cnse.printStackTrace(System.err);
		}
		return clone;
	}
	
	public void resetLoseStatus(){
		hasLost = false;
	}
}
