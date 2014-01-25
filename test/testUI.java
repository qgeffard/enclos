import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import com.enclos.component.HexagonPanel;
import com.enclos.component.BridgePanel;


public class testUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testUI window = new testUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public testUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 555, 447);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		HexagonPanel hexagonPanel = new HexagonPanel();
		hexagonPanel.setBounds(38, 275, 100, 100);
		frame.getContentPane().add(hexagonPanel);
		
		BridgePanel bridgePanel = new BridgePanel();
		bridgePanel.setBounds(71, 251, 30, 30);
		frame.getContentPane().add(bridgePanel);
	}
}
