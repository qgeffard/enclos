package test2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class ImageReSize extends JPanel {
	private static final int INIT_WIDTH = 400;
	private static final int MAX_WIDTH = 1200;

	private static final int TIMER_DELAY = 20;
	protected static final int WIDTH_STEP = 5;
	private int width = INIT_WIDTH;
	private int height = INIT_WIDTH;
	private boolean growing = true;

	public ImageReSize() {
		new Timer(TIMER_DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (growing && width < MAX_WIDTH) {
					width += WIDTH_STEP;
					height += WIDTH_STEP;
				} else {
					growing = false;
				}

				if (!growing && width > INIT_WIDTH) {
					width -= WIDTH_STEP;
					height -= WIDTH_STEP;
				} else {
					growing = true;
				}
				// get the parent container which is the scrollpane's viewport
				JComponent parent = (JComponent) getParent();
				parent.revalidate(); // let it relayout the changed size JPanel
				parent.repaint(); // and repaint all
			}
		}).start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		float x = (float) width / 10;
		float y = (float) height / 10;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(new GradientPaint(x, 0, Color.green, 0, y, Color.black,
				true));
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.setPaint(new GradientPaint(0, 0, Color.blue, x, y, Color.red, true));
		g2.fillOval(0, 0, width, height);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	private static void createAndShowUI() {
		ImageReSize imageReSize = new ImageReSize();
		JFrame frame = new JFrame("ImageReSize");
		frame.getContentPane().add(new JScrollPane(imageReSize));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				createAndShowUI();
			}
		});
	}
}