package com.enclos.data;

import java.awt.Image;
import java.util.Map;

import javax.swing.ImageIcon;

public class Player {
	
	private String firstName;
	private String lastName;
	private int age;
	
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
	
	
	
}
