package com.enclos.ui;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.enclos.data.Player;

public class PlayersMainPanel extends JPanel {

	private final Enclos enclos;
	private final FrameContentPane parent;
	private final PlayersGridPanel playersGridPanel;

	private boolean isSelectable = false;
	private final JPanel buttonPanel;
	private JButton next;

	public PlayersMainPanel(Enclos enclos, FrameContentPane parent) {
		this.enclos = enclos;
		this.parent = parent;

		this.setLayout(new BorderLayout());
		playersGridPanel = new PlayersGridPanel();

		buttonPanel = new JPanel();

		this.add(playersGridPanel, BorderLayout.NORTH);

		generatePlayersCard();

		JButton addPlayerButton = new JButton("Add player");

		next = new JButton("Next");
		next.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				int nbPlayers = PlayersMainPanel.this.playersGridPanel.getPlayerSelectedCount();

				if (nbPlayers >= 2) {
					Map<String, String> params = NewGameForm.display(PlayersMainPanel.this.playersGridPanel.getPlayerSelectedCount());
					if (params != null) {
						Long size = Long.valueOf(params.get("boardSize"));
						int nbSheep = Integer.valueOf(params.get("nbSheepPerPlayer"));
						Board newBoard = new Board(size, nbSheep, PlayersMainPanel.this.playersGridPanel.getPlayersSelected());

						if (params.get("close").equals("close")) {
							PlayersMainPanel.this.parent.resetGamePanel();
						}
						PlayersMainPanel.this.parent.addToGamePanel(newBoard);
						PlayersMainPanel.this.parent.goToGameGrid();

						PlayersMainPanel.this.playersGridPanel.reset();
					}
				} else {
					JOptionPane.showConfirmDialog(PlayersMainPanel.this, "Not enough players.");
				}
			}
		});

		// only workaround I found to keep the 'P' shortcut after clicking the
		// button
		addPlayerButton.setFocusable(false);

		addPlayerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				Player newPlayer = createNewPlayer();
				if (newPlayer != null) {
					PlayerProfilePanel playerProfile = new PlayerProfilePanel(newPlayer, PlayersMainPanel.this, PlayersMainPanel.this.isSelectable);
					PlayersMainPanel.this.playersGridPanel.addPlayerProfile(playerProfile);
					PlayersMainPanel.this.enclos.getPlayers().add(newPlayer);
					revalidate();
				}
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
						fileChooser.showDialog(PlayersMainPanel.this, "ok");
					}
				});
				final JComponent[] inputs = new JComponent[] { new JLabel("First Name"), firstName, new JLabel("Last Name"), lastName, new JLabel("Age"), age, profilPictureButton };
				JOptionPane.showMessageDialog(null, inputs, "Add a new player", JOptionPane.PLAIN_MESSAGE);
				try {
					File file = fileChooser.getSelectedFile();
					System.out.println(file.getAbsolutePath());
					newPlayer = new Player(firstName.getText(), lastName.getText(), Integer.parseInt(age.getText()), file.getAbsolutePath());

				} catch (Exception e) {
					return null;
				}
				return newPlayer;
			}
		});

		buttonPanel.add(addPlayerButton);
		buttonPanel.add(next);

		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	// TODO manage return carriage
	private void generatePlayersCard() {
		final List<Player> players = this.enclos.getPlayers();
		if (players != null && players.size() > 0) {
			for (final Player currentPlayer : players) {
				final PlayerProfilePanel playerProfile = new PlayerProfilePanel(currentPlayer, this, false);
				this.playersGridPanel.addPlayerProfile(playerProfile);
			}
		}
	}

	public void setSelectable(boolean isPlayerPanelSelectable) {
		this.isSelectable = isPlayerPanelSelectable;
		this.playersGridPanel.setSelectable(isPlayerPanelSelectable);
		if (isPlayerPanelSelectable) {
			this.next.setVisible(true);
		} else {
			this.next.setVisible(false);
		}
	}

	public Enclos getEnclos() {
		return this.enclos;
	}

	public PlayersGridPanel getGridPanel() {
		return this.playersGridPanel;
	}
}
