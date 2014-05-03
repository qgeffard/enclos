package com.enclos.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.enclos.resources.song.Speaker;

public class IntroFrame extends JFrame implements WindowListener {

	// in case we need it later
	private JFrame parent = null;
	private Dimension size = null;

	public IntroFrame(JFrame parent) {

		this.parent = parent;
		this.size = parent.getSize();
		setSize(this.size);
		setAlwaysOnTop(true);
		setUndecorated(true);
		
		getContentPane().setBackground(Color.BLACK); // FUCK THAT

		Image image = Toolkit.getDefaultToolkit().createImage("resources/image/intro.gif");
		ImageIcon icon = new ImageIcon(image);
		JLabel label = new JLabel(icon);
		this.getContentPane().add(label);

		// start a timer
		Thread t = new Timer();
		t.start();

		
		
		
		this.addWindowListener(this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);

	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		this.dispose();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	class Timer extends Thread {
		int time = 10;

		public void run() {
			try {
				sleep(1300);
				Speaker.playIntro();
				sleep(9000);
			} catch (InterruptedException e) {
			}
			// close the frame
			IntroFrame.this.processWindowEvent(new WindowEvent(IntroFrame.this, WindowEvent.WINDOW_CLOSED));
		}
	}

}
