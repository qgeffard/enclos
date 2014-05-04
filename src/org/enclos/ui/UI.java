package org.enclos.ui;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author Clement CARREAU
 * @author Quentin GEFFARD
 * @author Julien TELA
 */


public class UI {
	/**
	 *  main
	 * @param args
	 */
    public static void main(String[] args) {

        try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
        Enclos enclos = new Enclos();

    }
}