package com.mojang.ld22.sound;

import org.zilar.resource.ResourceLocation;

import java.applet.AudioClip;

public class Sound {
	public static final Sound playerHurt = new Sound(new ResourceLocation("minicraft:playerhurt.wav").getFilePath());
	public static final Sound playerDeath = new Sound(new ResourceLocation("minicraft:death.wav").getFilePath());
	public static final Sound monsterHurt = new Sound(new ResourceLocation("minicraft:monsterhurt.wav").getFilePath());
	public static final Sound test = new Sound(new ResourceLocation("minicraft:test.wav").getFilePath());
	public static final Sound pickup = new Sound(new ResourceLocation("minicraft:pickup.wav").getFilePath());
	public static final Sound bossdeath = new Sound(new ResourceLocation("minicraft:bossdeath.wav").getFilePath());
	public static final Sound craft = new Sound(new ResourceLocation("minicraft:craft.wav").getFilePath());

	private AudioClip clip;

	private Sound(String name) {
		/*
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		*/
	}

	public void play() {
		/*
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
			}.start();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		*/
	}
}