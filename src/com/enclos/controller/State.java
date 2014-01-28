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
		int widht = newSize.width;

		int check = checkContractOrExpand(newSize);

		//if we expand
		if (check == 1) {
			if (height > widht) {
				size = new Dimension(height, height);
			} else {
				size = new Dimension(widht, widht);
			}
		}else{
			if (height < widht) {
				size = new Dimension(height, height);
			} else {
				size = new Dimension(widht, widht);
			}
		}
		this.target.setSize(size);
	}

	private int checkContractOrExpand(Dimension newSize) {
		// since the frame is a square
		int oldSize = size.width;

		int newHeight = newSize.height;
		int newWidth = newSize.width;

		if ((oldSize < newHeight) || (oldSize < newWidth))
			return 1;
		else
			return -1;
	}
}