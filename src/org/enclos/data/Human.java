package org.enclos.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.enclos.component.Sheep;
import org.enclos.ui.Board;

public class Human extends Player implements Cloneable {
    private final int age;
    private int gamesWon = 0;
    private int gamesLost = 0;

    private String profilePicturePath;
    private BufferedImage profilePicture = null;

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

    public Human(String firstName, String lastName, int age, int gamesWon, int gamesLost, String picturePath) {
    	this(firstName,lastName,age,picturePath);
    	this.gamesWon = gamesWon;
    	this.gamesLost = gamesLost;
	}


    public int getAge() {
        return age;
    }

    public BufferedImage getProfilePicture() {
        return profilePicture;
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
    
    public void lose() {
        gamesLost++;
    }

	private void deepCopySheepList(){
		this.sheeps = new ArrayList<Sheep>();
	}
	
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
