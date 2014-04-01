package com.enclos.controller;

import java.awt.Dimension;

import javax.swing.JFrame;

public class State {

	private Dimension size;
	private JFrame target;

	public State(JFrame target) {
		this.target = target;
		this.size = target.getSize();
	}

	public void setSize(Dimension newSize, boolean fireEvent) {
		if (newSize.equals(size)) {
			return;
		}

		int height = newSize.height;
		int width = newSize.width;

		boolean check = checkExpand(newSize);

		//if we expand
		if (check) {
			size = (height > width) ? new Dimension(height, height) : new Dimension(width, width);
		}else{
			size = (height < width) ? new Dimension(height, height) : new Dimension(width, width);
		}
		this.target.setSize(size);
	}

	private boolean checkExpand(Dimension newSize) {
		// since the frame is a square
		int oldSize = size.width;

		int newHeight = newSize.height;
		int newWidth = newSize.width;

		if ((oldSize < newHeight) || (oldSize < newWidth))
			return true;
		else
			return false;
	}
}