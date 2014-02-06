import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.enclos.component.Hexagon;
import com.enclos.ui.Board;

public class Content extends JPanel {

	Polygon center = null;
	List<Point> centerPoints = new ArrayList<>();
	int edgeLength = 0;

	public Content() {
	}

	@Override
	public void paintComponent(Graphics g) {
		center = generateCenter(g);

		int dx = (int)(Math.cos(5.7595865315813) * 100);
		int dy = (int)(Math.sin(5.7595865315813) * 100);
		int x = 0;
		
		generateAnotherCell(g, dx, dy);
	}

	private void generateAnotherCell(Graphics g, int dx, int dy) {
		Polygon polygon = new Polygon();
		AffineTransform at = AffineTransform.getTranslateInstance(dx, dy);
		PathIterator pointsIterator = center.getPathIterator(at);
		while (!pointsIterator.isDone()) {
			double[] xy = new double[2];
			pointsIterator.currentSegment(xy);
			if (xy[0] == 0 && xy[1] == 0) {
				break;
			}
			polygon.addPoint((int) xy[0], (int) xy[1]);
			pointsIterator.next();
		}
		g.fillPolygon(polygon);

	}

	private Polygon generateCenter(Graphics g) {
		Polygon polygon = new Polygon();
		centerPoints.clear();

		for (int i = 0; i < 6; i++) {
			Point point = new Point(
					(int) (this.getWidth() / 2 + 50 * Math.cos(i * 2 * Math.PI
							/ 6)),
					(int) (this.getHeight() / 2 + 50 * Math.sin(i * 2 * Math.PI
							/ 6)));
			polygon.addPoint(point.x, point.y);
			centerPoints.add(point);
		}
		edgeLength = 50;
		g.fillPolygon(polygon);
		return polygon;
	}
}
