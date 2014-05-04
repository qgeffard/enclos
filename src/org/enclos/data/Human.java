package org.enclos.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.enclos.component.Sheep;
import org.enclos.ui.Board;

/**
 * @author Clement CARREAU
 * @author Quentin GEFFARD
 * @author Julien TELA
 */

public class Human extends Player implements Cloneable {
    private final int age;
    private int gamesWon = 0;
    private int gamesLost = 0;

    private String profilePicturePath;
    private BufferedImage profilePicture = null;
    
    /**
     * First constructor of Human who extends of Player
     * @param firstName
     * @param lastName
     * @param age
     */
    public Human(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        
        try {
			this.profilePicture = ImageIO.read(new File("resources/image/default_avatar.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Second Constructor of Human when we need to feed a external image as avatar
     * @param firstName
     * @param lastName
     * @param age
     * @param picturePath
     */
    public Human(String firstName, String lastName, int age, String picturePath) {
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
        }
    }
    
    /**
     * Third constructor call when to feed his score
     * @param firstName
     * @param lastName
     * @param age
     * @param gamesWon
     * @param gamesLost
     * @param picturePath
     */
    public Human(String firstName, String lastName, int age, int gamesWon, int gamesLost, String picturePath) {
    	this(firstName,lastName,age,picturePath);
    	this.gamesWon = gamesWon;
    	this.gamesLost = gamesLost;
	}

    
    /**
     * Getter age attribute
     * @return int age
     */
    public int getAge() {
        return age;
    }

    /**
     * Getter profile picture 
     * @return BufferedImage
     * @see BufferedImage
     */
    public BufferedImage getProfilePicture() {
        return profilePicture;
    }
    
    /**
     * Override toString method
     */
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
    
    /**
     * 	Getter profile picture path attribute
     * @return String
     */
    public String getProfilePicturePath() {
        return this.profilePicturePath;
    }
    
    /**
     * Getter number of game won by the Human
     * @return int number of games won
     */
    public int getNumberOfGamesWon(){
    	return this.gamesWon;
    }
    
    /**
     * Getter number of game lost by Human
     * @return int number of games lost
     */
    public int getNumberOfGamesLost(){
    	return this.gamesLost;
    }
    	
    /**
     * Used to increment the gamesWon attribute when Human win a game
     */
    public void win(){
    	this.gamesWon++;
    }
    
    /**
     * Used to increment the gamesLost attribute when Human lose a game
     */    
    public void lose() {
        gamesLost++;
    }

    /**
     * Used when Human is clone to clone his sheep list to
     */
	private void deepCopySheepList(){
		this.sheeps = new ArrayList<Sheep>();
	}
	
	/**
	 * Cloneable implementation 
	 */
	public Human clone() {
		Human clone = null;
		try {
			clone = (Human) super.clone();
			clone.deepCopySheepList();
		} catch(CloneNotSupportedException cnse) {
			cnse.printStackTrace(System.err);
		}
		return clone;
	}

}
