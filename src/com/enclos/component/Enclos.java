package com.enclos.component;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

import com.enclos.controller.State;
import com.enclos.ui.Board;

public class Enclos extends JFrame {

	private State state = null;

	public Enclos() {
		this.state = new State(this);
		
		Toolkit.getDefaultToolkit().setDynamicLayout(false);
		setMaximumSize(getScreenMaximumSize());
		setSize(500, 500);
		setContentPane(new Board(this));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		addResizeListener();
	}

	private Dimension getScreenMaximumSize() {
		Dimension maxDimension = null;
		Dimension screenDimension =Toolkit.getDefaultToolkit().getScreenSize();
		if(screenDimension.getHeight() > screenDimension.getWidth())
			return new Dimension(screenDimension.width,screenDimension.width);
		else return new Dimension(screenDimension.height, screenDimension.height);
	}

	private void addResizeListener() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				state.setSize(getSize(), true);
			}
		});
	}
}
