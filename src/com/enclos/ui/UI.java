package com.enclos.ui;

import javax.swing.*;

import com.enclos.component.Board;

public class UI {

	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		//TODO square frame to keep hexagone shape
		frame.setSize(900,900);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(new Board());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
}