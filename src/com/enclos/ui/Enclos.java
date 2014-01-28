package com.enclos.ui;

import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

import com.enclos.controller.State;

public class Enclos extends JFrame {

	private State state= null;
	
	public Enclos(){
		this.state = new State(this);
		
		//TODO fullSize
		
		Toolkit.getDefaultToolkit().setDynamicLayout(false);
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setContentPane(new Board(this));
		this.setVisible(true);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addResizeListener();
	}

	private void addResizeListener() {
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				state.setSize(getSize(), true);
			}
		});
		
	}
}
