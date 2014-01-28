package ui.back;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;

import components.back.Board;

public class UI {

	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		//TODO square frame to keep hexagone shape
		frame.setSize(900,900);
		
		//TODO dynamic ?
		frame.setMinimumSize(new Dimension(500,500));
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(new Board(2));
		//avoid bad dynamic resizing
		Toolkit.getDefaultToolkit().setDynamicLayout(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
}