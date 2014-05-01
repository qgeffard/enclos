package com.enclos.data;


public enum Difficulty {
	RAINBOW, NORMAL, HELL;

	@Override
	public String toString() {
		// only capitalize the first letter
		String s = super.toString();
		return s.substring(0, 1) + s.substring(1).toLowerCase();
	}
}
