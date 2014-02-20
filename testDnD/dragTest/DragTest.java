package dragTest;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.TransferHandler;

public class DragTest extends JFrame implements MouseMotionListener,
        MouseListener {

    private JPanel leftPanel = new JPanel(null);
    private JPanel rightPanel = new JPanel(null);
    JLabel dropLabel;

    public DragTest() {
        this.setLayout(new GridLayout(1, 2));

        leftPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        this.add(leftPanel);
        this.add(rightPanel);
        leftPanel.addMouseListener(this);
        leftPanel.addMouseMotionListener(this);

        JTextArea area = new JTextArea();

        rightPanel.setLayout(new GridLayout(1, 1));
        rightPanel.add(area);

        dropLabel = new JLabel("drop");
        dropLabel.setTransferHandler(new TransferHandler("text"));

    }
    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("mousePressed");
        Dimension labelSize = dropLabel.getPreferredSize();
        dropLabel.setSize(labelSize);
        int x = e.getX() - labelSize.width / 2;
        int y = e.getY() - labelSize.height / 2;
        dropLabel.setLocation(x, y);
        leftPanel.add(dropLabel);



        repaint();

    }

    @Override
    public void mouseDragged(MouseEvent me) {
        System.out.println("mouseDragged");
            dropLabel.getTransferHandler().exportAsDrag(dropLabel, me,
                    TransferHandler.COPY);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("mouseClicked");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("mouseReleased");

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public static void maian(String[] args) {

        DragTest frame = new DragTest();
        frame.setVisible(true);
        frame.setSize(600, 400);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
