package com.enclos.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
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
		setLayout(new BorderLayout());
		
		Image image = Toolkit.getDefaultToolkit().createImage("resources/image/team.jpg");
		ImageIcon icon = new ImageIcon(image);
		JLabel label = new JLabel(icon);

		JLabel label2 = new JLabel("Developper team : ");
		label2.setText("<html>"
						+ "<h1>Developper Team : </h1><br>"
						+ "<ul>"
							+ "<li>CARREAU CLEMENT</li>"
							+ "<li>TELA JULIEN </li>"
							+ "<li>GEFFARD QUENTIN</li>"
						+ "</ul>"
					    + "<br>"
					+ "</html>");
		add(label2, BorderLayout.NORTH);
		add(label, BorderLayout.CENTER);
		
	}

}