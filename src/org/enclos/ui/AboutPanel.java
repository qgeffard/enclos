package org.enclos.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class AboutPanel extends JPanel {


	public AboutPanel() {
		Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder title = BorderFactory.createTitledBorder(lowerEtched, "About Enclos");
		setBorder(title);
		
		feedTable();
	}

	public void feedTable() {
		this.removeAll();
		
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		
		Image image = Toolkit.getDefaultToolkit().createImage("resources/image/team.jpg");
		ImageIcon icon = new ImageIcon(image);
		JLabel imgDev = new JLabel(icon);

		JLabel developpers = new JLabel("Developper team : ");
		developpers.setText("<html>"
						+ "<h1><u>Enclos Game</u></h1>"
						+ "<span><i>&nbsp;&nbsp;&nbsp;This project was...</i></span>"
				        + "<h2>Specifications</h2>"
				        + "<ul>"
							+ "<li>GAVA F.</li>"
					    + "</ul>"
						+ "<h2>Developper Team : </h2><br>"
						+ "<ul>"
							+ "<li>CARREAU CLEMENT</li>"
							+ "<li>TELA JULIEN </li>"
							+ "<li>GEFFARD QUENTIN</li>"
						+ "</ul>"
					    + "<br>"
					+ "</html>");
		content.add(developpers);
		content.add(imgDev);
		
		add(content);
	}

}