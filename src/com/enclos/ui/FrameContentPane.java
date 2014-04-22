package com.enclos.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.enclos.data.Player;

public class FrameContentPane extends JPanel {
    	
	private CardLayout cardLayout;
    private JPanel gamePanel;
    private JPanel playersPanel;
    private PlayersGridPanel playersGridPanel;
    private JPanel playersChooser;
    private Enclos parent;

    private String GAMEPANELNAME = "GamePanel";

    private String PLAYERGRIDNAME = "PlayersGrid";
    
    public FrameContentPane(Enclos parent) {
        this.parent = parent;

        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        playersGridPanel = new PlayersGridPanel();
        
        gamePanel = new JPanel();
        gamePanel.setLayout(new FlowLayout());

        playersPanel = new JPanel();
        playersPanel.setLayout(new BorderLayout());
        

        //TODO manage return carriage
        playersPanel.add(playersGridPanel,BorderLayout.NORTH);
        generatePlayersCard(parent);

        JButton addPlayerButton = new JButton("Add player");
        
        // only workaround I found to keep the 'P' shortcut after clicking the button
        addPlayerButton.setFocusable(false);

        addPlayerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Player newPlayer = createNewPlayer();
                PlayerProfilePanel playerProfile = new PlayerProfilePanel(newPlayer, FrameContentPane.this, false);
                FrameContentPane.this.playersGridPanel.addPlayerProfile(playerProfile);
                FrameContentPane.this.parent.getPlayers().add(newPlayer);
                revalidate();
            }

            private Player createNewPlayer() {
                Player newPlayer = null;
                final JTextField firstName = new JTextField();
                final JTextField lastName = new JTextField();
                final JTextField age = new JTextField();
                final JButton profilPictureButton = new JButton("Choose a profile picture");

                final JFileChooser fileChooser = new JFileChooser();

                final FileDialog fileDialog = new FileDialog(new JFrame(), "Choose picture", FileDialog.LOAD);
                profilPictureButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // fileDialog.setVisible(true);
                        fileChooser.showDialog(FrameContentPane.this, "ok");
                    }
                });
                final JComponent[] inputs = new JComponent[] { new JLabel("First Name"), firstName, new JLabel("Last Name"), lastName,
                        new JLabel("Age"), age, profilPictureButton };
                JOptionPane.showMessageDialog(null, inputs, "Add a new player", JOptionPane.PLAIN_MESSAGE);
                try {
                    File file = fileChooser.getSelectedFile();
                    System.out.println(file.getAbsolutePath());
                    newPlayer = new Player(firstName.getText(), lastName.getText(), Integer.parseInt(age.getText()), file.getAbsolutePath());

                } catch (Exception e) {
                    newPlayer = new Player(firstName.getText(), lastName.getText(), 0);
                }
                return newPlayer;
            }
        });

        playersPanel.add(addPlayerButton, BorderLayout.SOUTH);
        
        this.add(gamePanel, GAMEPANELNAME);
        this.add(playersPanel, PLAYERGRIDNAME);        
    }

    private void generatePlayersCard(final Enclos parent) {
		final List<Player> players = parent.getPlayers();
		if(players != null && players.size() > 0){
			for(final Player currentPlayer : players){
				final PlayerProfilePanel playerProfile = new PlayerProfilePanel(currentPlayer, this, false);
		        FrameContentPane.this.playersGridPanel.addPlayerProfile(playerProfile);
			}
		}

	}

    public void addToGamePanel(Board board) {
        gamePanel.add(board);
    }

    public void resetGamePanel() {
        gamePanel.removeAll();
    }

    public void switchPanel() {
        cardLayout.next(this);
    }
    
    public void goToPlayersGrid() {
        cardLayout.show(this,PLAYERGRIDNAME);
    }
	
	public Enclos getParent(){
		return this.parent;
	}
    
	public PlayersGridPanel getPlayersGrid(){
		return this.playersGridPanel;
	}
}
