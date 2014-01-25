package com.enclos.ui;

import java.awt.Dimension;

import javax.swing.*;

import com.enclos.component.Board;

public class UI {

	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		//TODO square frame to keep hexagone shape
		frame.setSize(900,900);
		
		//TODO dynamic ?
		frame.setMinimumSize(new Dimension(500,500));
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(new Board());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
}