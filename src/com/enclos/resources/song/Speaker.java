package com.enclos.resources.song;

import java.applet.Applet;
import java.applet.AudioClip;
import java.util.LinkedList;
import java.util.List;

import com.enclos.ui.Board;

public class Speaker {
	private static List<AudioClip> saySheep = new LinkedList<AudioClip>();
	private static List<AudioClip> stepSheep = new LinkedList<AudioClip>();
	private static List<AudioClip> dropBarrier = new LinkedList<AudioClip>();
	private static boolean isMute = false;

	static{
		// SAY
		Speaker.saySheep.add(Applet.newAudioClip(Board.class
				.getResource("/com/enclos/resources/song/say1.wav")));
		Speaker.saySheep.add(Applet.newAudioClip(Board.class
				.getResource("/com/enclos/resources/song/say2.wav")));
		Speaker.saySheep.add(Applet.newAudioClip(Board.class
				.getResource("/com/enclos/resources/song/say3.wav")));

		// STEP
		Speaker.stepSheep.add(Applet.newAudioClip(Board.class
				.getResource("/com/enclos/resources/song/step1.wav")));
		Speaker.stepSheep.add(Applet.newAudioClip(Board.class
				.getResource("/com/enclos/resources/song/step2.wav")));
		Speaker.stepSheep.add(Applet.newAudioClip(Board.class
				.getResource("/com/enclos/resources/song/step3.wav")));
		Speaker.stepSheep.add(Applet.newAudioClip(Board.class
				.getResource("/com/enclos/resources/song/step4.wav")));
		Speaker.stepSheep.add(Applet.newAudioClip(Board.class
				.getResource("/com/enclos/resources/song/step5.wav")));

		// DROP BARRIER
		Speaker.dropBarrier.add(Applet.newAudioClip(Board.class
				.getResource("/com/enclos/resources/song/wood1.wav")));
		Speaker.dropBarrier.add(Applet.newAudioClip(Board.class
				.getResource("/com/enclos/resources/song/wood2.wav")));
		Speaker.dropBarrier.add(Applet.newAudioClip(Board.class
				.getResource("/com/enclos/resources/song/wood3.wav")));
		Speaker.dropBarrier.add(Applet.newAudioClip(Board.class
				.getResource("/com/enclos/resources/song/wood4.wav")));
		Speaker.dropBarrier.add(Applet.newAudioClip(Board.class
				.getResource("/com/enclos/resources/song/wood5.wav")));
		Speaker.dropBarrier.add(Applet.newAudioClip(Board.class
				.getResource("/com/enclos/resources/song/wood6.wav")));
	}

	public static void playRandomSaySheep() {
		if (!Speaker.isMute) {
			int max = Speaker.saySheep.size();
			Speaker.saySheep.get((int) (Math.random() * (max - 0)) + 0).play();
		}
	}

	public void playRandomStep() {
		if (!Speaker.isMute) {
			int max = Speaker.stepSheep.size();
			Speaker.stepSheep.get((int) (Math.random() * (max - 0)) + 0).play();
		}
	}

	public static void playRandomDropBarrier() {
		if (!Speaker.isMute) {
			int max = Speaker.dropBarrier.size();
			Speaker.dropBarrier.get((int) (Math.random() * (max - 0)) + 0).play();
		}
	}

	public static void isMute(boolean isMute){
		Speaker.isMute = isMute;
	}
}
