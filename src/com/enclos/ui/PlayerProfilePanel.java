package com.enclos.ui;

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

import com.enclos.data.Player;
import com.enclos.data.PlayerProfilePanelState;

public class PlayerProfilePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private PlayerProfilePanelState state = PlayerProfilePanelState.DISABLE;
    private final JPanel imagePanel;

    private Color baseColor = null;
    private PlayersMainPanel parent = null;
    private Player player = null;

    public PlayerProfilePanel(final Player player, final PlayersMainPanel parent, boolean isSelectingEnabled) {

    	this.player = player;
        if (isSelectingEnabled) {
            state = PlayerProfilePanelState.NOTSELECTED;
        }

        this.parent = parent;
        this.imagePanel = new JPanel();

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(150,150));
        this.add(imagePanel);
        this.add(infoPanel);
        

        imagePanel.add(new JLabel(new ImageIcon(getProfilePicture(player.getProfilePicture()))));
        infoPanel.add(new JLabel(player.getLastName()));
        infoPanel.add(new JLabel(player.getFirstName()));
        infoPanel.add(new JLabel(String.valueOf(player.getAge())+" ans"));
        infoPanel.add(new JLabel("Games won : "+String.valueOf(player.getNumberOfGamesWon())));
        infoPanel.add(new JLabel("Games lost : "+String.valueOf(player.getNumberOfGamesLost())));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    int choice = JOptionPane.showConfirmDialog(parent, "Delete " + player.getLastName() + " " + player.getFirstName() + "?");

                    if (choice == JOptionPane.OK_OPTION) {
                        parent.getEnclos().getPlayers().remove(player);
                        parent.getGridPanel().remove(PlayerProfilePanel.this);
                        parent.getGridPanel().revalidate();
                        parent.getGridPanel().repaint();
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
        Image image = profilePicture.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        return toBufferedImage(image);
    }

    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

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

    public void setSelectable(boolean isSelectable) {
        if (isSelectable) {
            this.state = PlayerProfilePanelState.NOTSELECTED;
        } else {
            this.state = PlayerProfilePanelState.DISABLE;
            this.imagePanel.setBackground(this.baseColor);
        }
    }
    
    public PlayerProfilePanelState getState(){
    	return this.state;
    }
    
    public Player getPlayer(){
    	return this.player;
    }
    
    public void unSelect()
    {
        this.state = PlayerProfilePanelState.NOTSELECTED;
        this.imagePanel.setBackground(this.baseColor);
    }
}