package components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import controller.HexagonListener;

public class HexagonPanel extends JPanel {

	private static int averageEdgeLength = 0;
	private List<Point> pointList = new ArrayList<>();
	
	private static final long serialVersionUID = 1L;
	// Default size
	// TODO dynamic from the board's size
	private int width = 50;
	private int height = 50;

	public HexagonPanel() {
		setOpaque(false);
		// setComponentZOrder(this.getParent(), 0);
		addMouseListener(new HexagonListener());

	}

	// return the new hexagon size from the new Board's size
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		pointList.clear();
		
		// hexagon's size depending of the board's size
		int frameWidth = (getParent().getBounds().width) / 10;
		int frameHeight = (getParent().getBounds().height) / 10;

		Polygon tile = new Polygon();

		for (int i = 0; i < 6; i++) {
			Point point = new Point((int) (frameWidth + frameWidth
					* Math.cos(i * 2 * Math.PI / 6)),
					(int) (frameHeight + frameHeight
							* Math.sin(i * 2 * Math.PI / 6)));
			tile.addPoint(point.x, point.y);
			pointList.add(point);
		}

		g.drawPolygon(tile);
		g.fillPolygon(tile);

		// since the jpanel is a square, we need to keep the max(width,height)
		// of the hexagon to surround it, otherwise it would be cut
		int maxLength = Math.max(tile.getBounds().width,
				tile.getBounds().height);
		this.width = maxLength;
		this.height = maxLength;
		
		calculateAverageEdge();
	}

	private void calculateAverageEdge() {
		
			double totalLength = 0;
			for(int i=0; i<=5; i++){
				if(i==5){
					totalLength +=  Math.sqrt(Math.pow((pointList.get(i).x)-(pointList.get(0).x),2)+Math.pow((pointList.get(i).y)-(pointList.get(0).y),2));
				}
				else totalLength += Math.sqrt(Math.pow((pointList.get(i+1).x)-(pointList.get(i).x),2)+Math.pow((pointList.get(i+1).y)-(pointList.get(i).y),2));
			}
			
			HexagonPanel.averageEdgeLength = (int)(totalLength/6);
			System.out.println(HexagonPanel.averageEdgeLength);
		
		
	}
	
	public void resizeHexagon(int posX, int posY){
		this.setBounds(posX-this.width/2, posY-this.height/2, this.width, this.height);
		
	}
	
	public static void clearAverageEdge(){
		HexagonPanel.averageEdgeLength = 0;
	}
	
	public static int getAverageEdge(){
		return HexagonPanel.averageEdgeLength;
	}
}
