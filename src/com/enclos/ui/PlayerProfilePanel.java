package com.enclos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.enclos.data.Player;

public class PlayerProfilePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public PlayerProfilePanel(Player player) {

		JPanel imagePanel = new JPanel();

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		this.add(imagePanel, BorderLayout.WEST);
		this.add(infoPanel, BorderLayout.EAST);

		imagePanel.add(new JLabel(new ImageIcon(getProfilePicture(player
				.getProfilePicture()))));
		infoPanel.add(new JLabel(player.getLastName()));
		infoPanel.add(new JLabel(player.getFirstName()));
		infoPanel.add(new JLabel(String.valueOf(player.getAge())));
	}

	private BufferedImage getProfilePicture(BufferedImage profilePicture) {
		Image image = profilePicture.getScaledInstance(100, 100,
				Image.SCALE_SMOOTH);
		return toBufferedImage(image);
	}

	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null),
				img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}

}
