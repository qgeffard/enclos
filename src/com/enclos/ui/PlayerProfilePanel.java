package com.enclos.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.enclos.data.Player;

public class PlayerProfilePanel extends JPanel{

	private Image image = null;
	private String lastName;
	private String firstName;
	private int age;
	
	public PlayerProfilePanel(Image image){
		this.image = image;
	}
	
	public PlayerProfilePanel(Player player){
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		//Border border = new LineBorder(Color.red);
		//this.setBorder(border);
		this.lastName= player.getLastName();
		this.firstName = player.getFirstName();
		this.age = player.getAge();
		
		
		this.add(new JLabel(lastName));
		this.add(new JLabel(firstName));
		this.add(new JLabel(String.valueOf(age)));
	}
	
	@Override
	public void paintComponent(Graphics g){
		//g.drawImage(this.image, 0, 0, null);
	//	setBackground(Color.red);
		 super.paintComponent(g);
	        Graphics2D g2d = (Graphics2D) g;
	        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	        int w = getWidth();
	        int h = getHeight();
	        Color color1 = Color.GRAY;
	        Color color2 = Color.WHITE;
	        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
	        g2d.setPaint(gp);
	        g2d.fillRect(0, 0, w, h);
	}
}
