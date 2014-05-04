package org.enclos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.enclos.data.Human;
import org.enclos.data.PlayerProfilePanelState;

/**
 * @author Clement CARREAU
 * @author Quentin GEFFARD
 * @author Julien TELA
 */

public class PlayerProfilePanel extends JPanel {

	private PlayerProfilePanelState state = PlayerProfilePanelState.NOTSELECTED;
	private final JPanel imagePanel;

	private Color baseColor = null;
	private PlayersMainPanel parent = null;
	private Human player = null;

	/**
	 * Constructor of the PlayerProfilePanel class
	 * @param player
	 * @param parent
	 */
	public PlayerProfilePanel(final Human player, final PlayersMainPanel parent) {

		this.player = player;

		this.parent = parent;
		this.imagePanel = new JPanel();

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		this.setPreferredSize(new Dimension(150, 150));
		this.add(imagePanel);
		this.add(infoPanel);

		imagePanel.add(new JLabel(new ImageIcon(getProfilePicture(player.getProfilePicture()))));
		infoPanel.add(new JLabel(player.getLastName()));
		infoPanel.add(new JLabel(player.getFirstName()));
		infoPanel.add(new JLabel(String.valueOf(player.getAge()) + " ans"));
		infoPanel.add(new JLabel("Games won : " + String.valueOf(player.getNumberOfGamesWon())));
		infoPanel.add(new JLabel("Games lost : " + String.valueOf(player.getNumberOfGamesLost())));

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if (SwingUtilities.isLeftMouseButton(e)) {
					PlayerProfilePanel.this.toggleState();
				}
			}

		});

	}

	private BufferedImage getProfilePicture(BufferedImage profilePicture) {
		Image image = profilePicture.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		return toBufferedImage(image);
	}

	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		return bimage;
	}
	
	/**
	 * Override the paint component method
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.baseColor = this.getBackground();
	}
	
	/**
	 * Used to show the select effect to the user (green border)
	 */
	public void toggleState() {
		if (this.state == PlayerProfilePanelState.NOTSELECTED) {
			this.state = PlayerProfilePanelState.SELECTED;
			this.imagePanel.setBackground(Color.green);
		} else {
			this.state = PlayerProfilePanelState.NOTSELECTED;
			this.imagePanel.setBackground(this.baseColor);

		}
	}
	
	/**
	 * Getter of state attribute 
	 * @return state
	 */
	public PlayerProfilePanelState getState() {
		return this.state;
	}

	/**
	 * Getter of player attribute
	 * @return player 
	 */
	public Human getPlayer() {
		return this.player;
	}

	/**
	 * deselect the profile planel
	 */
	public void unSelect() {
		this.state = PlayerProfilePanelState.NOTSELECTED;
		this.imagePanel.setBackground(this.baseColor);
	}
}