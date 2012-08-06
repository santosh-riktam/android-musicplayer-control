package com.san.mediaplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ToggleButton;

public class MediaPlayerTweakActivity extends Activity {
	private Button prevButton, stopButton, nextButton;
	private ToggleButton playPauseButton;

	public static final String SERVICECMD = "com.android.music.musicservicecommand";
	public static final String CMDNAME = "command";
	public static final String CMDTOGGLEPAUSE = "togglepause";
	public static final String CMDSTOP = "stop";
	public static final String CMDPAUSE = "pause";
	public static final String CMDPREVIOUS = "previous";
	public static final String CMDNEXT = "next";
	private AudioManager audioManager;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_FULLSCREEN
						| LayoutParams.FLAG_TURN_SCREEN_ON
						| LayoutParams.FLAG_DISMISS_KEYGUARD);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		prevButton = (Button) findViewById(R.id.prevButton);
		playPauseButton = (ToggleButton) findViewById(R.id.playPauseButton);
		stopButton = (Button) findViewById(R.id.stopButton);
		nextButton = (Button) findViewById(R.id.nextButton);

		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

	}

	private void updatePlayButtonImage() {
		if (audioManager.isMusicActive())
			playPauseButton.setChecked(true);
		else
			playPauseButton.setChecked(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updatePlayButtonImage();
	}

	public void onMediaButtonClicked(final View v) {
		String command = "";
		switch (v.getId()) {
		case R.id.prevButton:
			command = CMDPREVIOUS;
			break;
		case R.id.playPauseButton:
			command = CMDTOGGLEPAUSE;
			break;
		case R.id.stopButton:
			command = CMDSTOP;
			break;
		case R.id.nextButton:
			command = CMDNEXT;
			break;

		default:
			break;
		}

		Intent intent = new Intent(SERVICECMD);
		intent.putExtra(CMDNAME, command);
		sendBroadcast(intent);
	}

}