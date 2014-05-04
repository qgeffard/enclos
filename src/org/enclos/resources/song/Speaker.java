package org.enclos.resources.song;

import java.applet.Applet;
import java.applet.AudioClip;
import java.util.LinkedList;
import java.util.List;

import org.enclos.ui.EnclosMenu;

/**
 * @author Clement CARREAU
 * @author Quentin GEFFARD
 * @author Julien TELA
 */

public class Speaker {
	private AudioClip music;
	private static List<AudioClip> saySheep = new LinkedList<AudioClip>();
	private static List<AudioClip> stepSheep = new LinkedList<AudioClip>();
	private static List<AudioClip> dropBarrier = new LinkedList<AudioClip>();
	private static List<AudioClip> hoverEvent = new LinkedList<AudioClip>();
	private static AudioClip intro;
	private static boolean isMute = false;
	private static boolean isMusicPlaying = false;
	
	/**
	 *  Constructor of Speaker class
	 */
	public Speaker() {
		this.music = Applet.newAudioClip(Speaker.class.getResource("/org/enclos/resources/song/tetris_music.wav"));
	}
	
	/**
	 * Feed all list of media with song 
	 */
	static {
		// SAY
		Speaker.saySheep.add(Applet.newAudioClip(Speaker.class.getResource("/org/enclos/resources/song/say1.wav")));
		Speaker.saySheep.add(Applet.newAudioClip(Speaker.class.getResource("/org/enclos/resources/song/say2.wav")));
		Speaker.saySheep.add(Applet.newAudioClip(Speaker.class.getResource("/org/enclos/resources/song/say3.wav")));


		// DROP BARRIER
		Speaker.dropBarrier.add(Applet.newAudioClip(Speaker.class.getResource("/org/enclos/resources/song/wood1.wav")));
		Speaker.dropBarrier.add(Applet.newAudioClip(Speaker.class.getResource("/org/enclos/resources/song/wood2.wav")));
		Speaker.dropBarrier.add(Applet.newAudioClip(Speaker.class.getResource("/org/enclos/resources/song/wood3.wav")));
		Speaker.dropBarrier.add(Applet.newAudioClip(Speaker.class.getResource("/org/enclos/resources/song/wood4.wav")));
		Speaker.dropBarrier.add(Applet.newAudioClip(Speaker.class.getResource("/org/enclos/resources/song/wood5.wav")));
		Speaker.dropBarrier.add(Applet.newAudioClip(Speaker.class.getResource("/org/enclos/resources/song/wood6.wav")));

		// Intro
		Speaker.intro = Applet.newAudioClip(Speaker.class.getResource("/org/enclos/resources/song/intro.wav"));
	}
	
	/**
	 * Play random say sheep
	 */
	public static void playRandomSaySheep() {
		if (!Speaker.isMute) {
			int max = Speaker.saySheep.size();
			Speaker.saySheep.get((int) (Math.random() * (max - 0)) + 0).play();
		}
	}
	
	/**
	 * Play random drop barrier song
	 */
	public static void playRandomDropBarrier() {
		if (!Speaker.isMute) {
			int max = Speaker.dropBarrier.size();
			Speaker.dropBarrier.get((int) (Math.random() * (max - 0)) + 0).play();
		}
	}

	/**
	 * Play intro music
	 */
	public static void playIntro() {
		if (!Speaker.isMute) {
			Speaker.intro.play();
		}
	}
	
	/**
	 * Start music tetris theme
	 */
	public void playMusic() {
		if (!Speaker.isMusicPlaying && EnclosMenu.isMusicActivated) {
			music.loop();
			Speaker.isMusicPlaying = true;
		}
	}
	
	/**
	 * Stop music tetris theme
	 */
	public void stopMusic() {
		music.stop();
		Speaker.isMusicPlaying = false;
	}
	
	/**
	 * Used to change the mute status of Speaker with the boolean passed as argument
	 * @param isMute
	 */
	public static void isMute(boolean isMute) {
		Speaker.isMute = isMute;
	}
}
