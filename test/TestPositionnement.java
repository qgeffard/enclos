import javax.swing.JFrame;


public class TestPositionnement {
	public static void main(String[] args){
		
		JFrame frame = new JFrame();
		frame.setContentPane(new Content());
		frame.setSize(500,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}
}
