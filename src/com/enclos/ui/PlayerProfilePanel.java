package com.enclos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
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

import com.enclos.data.Player;

public class PlayerProfilePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private PlayerProfilePanelState state = PlayerProfilePanelState.DISABLE;
	private JPanel imagePanel;

	private Color baseColor = null;
	private FrameContentPane parent = null;

	public PlayerProfilePanel(final Player player,
			FrameContentPane frameContentPane, boolean isSelectingEnabled) {

		if (isSelectingEnabled) {
			state = PlayerProfilePanelState.NOTSELECTED;
		}

		this.parent = frameContentPane;
		this.imagePanel = new JPanel();

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		this.add(imagePanel, BorderLayout.WEST);
		this.add(infoPanel, BorderLayout.EAST);

		imagePanel.add(new JLabel(new ImageIcon(getProfilePicture(player
				.getProfilePicture()))));
		infoPanel.add(new JLabel(player.getLastName()));
		infoPanel.add(new JLabel(player.getFirstName()));
		infoPanel.add(new JLabel(String.valueOf(player.getAge())));

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (SwingUtilities.isRightMouseButton(e)) {
					int choice = JOptionPane.showConfirmDialog(
							parent,
							"Delete " + player.getLastName() + " "
									+ player.getFirstName() + "?");

					if (choice == JOptionPane.OK_OPTION) {
						parent.getParent().getPlayers().remove(player);
						parent.getPlayersGrid().remove(PlayerProfilePanel.this);
						parent.getPlayersGrid().revalidate();
						parent.getPlayersGrid().repaint();
					}
				}
			}

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

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.baseColor = this.getBackground();
	}

	public void toggleState() {
		if (this.state != PlayerProfilePanelState.DISABLE) {
			if (this.state == PlayerProfilePanelState.NOTSELECTED) {
				this.state = PlayerProfilePanelState.SELECTED;
				this.imagePanel.setBackground(Color.green);
			} else {
				this.state = PlayerProfilePanelState.NOTSELECTED;
				this.imagePanel.setBackground(this.baseColor);
			}
		}
	}
	
	public void setSelectable(boolean isSelectable){
		if(isSelectable){
		this.state = PlayerProfilePanelState.NOTSELECTED;
		}else{
		this.state = PlayerProfilePanelState.DISABLE;
		}
	}

	private enum PlayerProfilePanelState {
		DISABLE, SELECTED, NOTSELECTED;
	}
}