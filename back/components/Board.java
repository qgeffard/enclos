package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import data.BridgeRotation;

public class Board extends JPanel {

	private static final long serialVersionUID = 1L;

	final HexagonPanel center = new HexagonPanel();
	private List<HexagonPanel> hexagons = new ArrayList<>();
	private List<BridgePanel> bridges = new ArrayList<>();

	public Board(int size) {

		add(center);
		createHexagonsList(size);
		createBridgesList(size);

		// when the frame is resized, the board is resized too
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				
				// we keep the board square
				initializeBoard();

			}
		});

		// setComponentZOrder(hex, 1);0
		// setComponentZOrder(bridge, 0);

		// absolute positioning, no layout manager
		setLayout(null);
	}

	private void createBridgesList(int size) {
		int nbBridges = 0;
		for (int i = 1; i < 3 * size; i += 3) {
			nbBridges += 6 * i;
		}
		nbBridges += size * 6;

		for (int j = 0; j < nbBridges; j++) {
			BridgePanel currentBridgePanel = new BridgePanel();
			bridges.add(currentBridgePanel);
			add(currentBridgePanel);
		}
	}

	private void createHexagonsList(int size) {
		int nbHexagones = 0;
		for (int i = 1; i <= size; i++)
			nbHexagones += 6 * i;

		for (int j = 0; j < nbHexagones; j++) {
			HexagonPanel currentHexPanel = new HexagonPanel();
			hexagons.add(currentHexPanel);
			add(currentHexPanel);
		}
	}

	private void initializeBoard() {
		// TODO conventions de nommage Hexagones (ex centre : 0,0, celui du
		// dessus : 1,0, etc ...)

		int min = Math.min(this.getParent().getWidth(), this.getParent()
				.getHeight());
		setSize(min, min);

		// first hexagon centered
		center.resizeHexagon(getWidth() / 2, getHeight() / 2);
		BridgePanel.setEdge(center.getAverageEdge());

		// TODO replace 100 by hex's max edge
		BridgePanel bridge = bridges.get(0);
		bridge.setRotation(BridgeRotation.DOWNWARD.getRotation());

		Dimension t = bridge.getPreferredSize();
		bridge.setBounds(275, 275, (int) (100 * Math.sqrt(2)),
				(int) (100 * Math.sqrt(2)));

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// differentiate the board from the frame
		setBackground(Color.GRAY);
	}

}
