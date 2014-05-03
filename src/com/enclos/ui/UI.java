package com.enclos.ui;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class UI {
    public static void main(String[] args) {

        // frame normale avec le resize non dynamique
        // UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Enclos enclos = new Enclos();

    }
}