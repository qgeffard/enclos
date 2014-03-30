package com.enclos.resources.song;

import java.applet.Applet;
import java.applet.AudioClip;
import java.util.LinkedList;
import java.util.List;

import com.enclos.ui.Board;

public class Speaker {
	private List<AudioClip> saySheep = new LinkedList<AudioClip>();
	private List<AudioClip> stepSheep = new LinkedList<AudioClip>();
	private List<AudioClip> dropBarrier = new LinkedList<AudioClip>();
	
	public Speaker(){
		//SAY
		this.saySheep.add(Applet.newAudioClip(Board.class.getResource("/com/enclos/resources/song/say1.wav")));
		this.saySheep.add(Applet.newAudioClip(Board.class.getResource("/com/enclos/resources/song/say2.wav")));
		this.saySheep.add(Applet.newAudioClip(Board.class.getResource("/com/enclos/resources/song/say3.wav")));
		
		//STEP
		this.stepSheep.add(Applet.newAudioClip(Board.class.getResource("/com/enclos/resources/song/step1.wav")));
		this.stepSheep.add(Applet.newAudioClip(Board.class.getResource("/com/enclos/resources/song/step2.wav")));
		this.stepSheep.add(Applet.newAudioClip(Board.class.getResource("/com/enclos/resources/song/step3.wav")));
		this.stepSheep.add(Applet.newAudioClip(Board.class.getResource("/com/enclos/resources/song/step4.wav")));
		this.stepSheep.add(Applet.newAudioClip(Board.class.getResource("/com/enclos/resources/song/step5.wav")));
		
		//DROP BARRIER
		this.dropBarrier.add(Applet.newAudioClip(Board.class.getResource("/com/enclos/resources/song/wood1.wav")));
		this.dropBarrier.add(Applet.newAudioClip(Board.class.getResource("/com/enclos/resources/song/wood2.wav")));
		this.dropBarrier.add(Applet.newAudioClip(Board.class.getResource("/com/enclos/resources/song/wood3.wav")));
		this.dropBarrier.add(Applet.newAudioClip(Board.class.getResource("/com/enclos/resources/song/wood4.wav")));
		this.dropBarrier.add(Applet.newAudioClip(Board.class.getResource("/com/enclos/resources/song/wood5.wav")));
		this.dropBarrier.add(Applet.newAudioClip(Board.class.getResource("/com/enclos/resources/song/wood6.wav")));
	}
	
	public void playRandomSaySheep(){
		int max = this.saySheep.size();
		this.saySheep.get((int)(Math.random() * (max-0)) + 0).play();
	}
	
	public void playRandomStep(){
		int max = this.stepSheep.size();
		this.stepSheep.get((int)(Math.random() * (max-0)) + 0).play();
	}
	
	public void playRandomDropBarrier(){
		int max = this.dropBarrier.size();
		this.dropBarrier.get((int)(Math.random() * (max-0)) + 0).play();
	}
	
}
