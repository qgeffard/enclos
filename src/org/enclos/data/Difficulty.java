package org.enclos.data;

/**
 * @author Clement CARREAU
 * @author Quentin GEFFARD
 * @author Julien TELA
 */

public enum Difficulty {
	RAINBOW, NORMAL;
	
    /**
     * Override toString
     *  @return string
     */
	@Override
	public String toString() {
		// only capitalize the first letter
		String s = super.toString();
		return s.substring(0, 1) + s.substring(1).toLowerCase();
	}
}
