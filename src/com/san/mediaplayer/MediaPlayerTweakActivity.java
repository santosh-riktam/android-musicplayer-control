package com.san.mediaplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.style.UpdateAppearance;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

public class MediaPlayerTweakActivity extends Activity {
	Button prevButton, playPauseButton, stopButton, nextButton;

	public static final String SERVICECMD = "com.android.music.musicservicecommand";
	public static final String CMDNAME = "command";
	public static final String CMDTOGGLEPAUSE = "togglepause";
	public static final String CMDSTOP = "stop";
	public static final String CMDPAUSE = "pause";
	public static final String CMDPREVIOUS = "previous";
	public static final String CMDNEXT = "next";
	private AudioManager audioManager;
	Handler handler;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		prevButton = (Button) findViewById(R.id.prevButton);
		playPauseButton = (Button) findViewById(R.id.playPauseButton);
		stopButton = (Button) findViewById(R.id.stopButton);
		nextButton = (Button) findViewById(R.id.nextButton);

		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		handler = new Handler();
	}

	private void updatePlayButtonImage() {
		if (audioManager.isMusicActive())
			playPauseButton.setBackgroundResource(R.drawable.pause);
		else
			playPauseButton.setBackgroundResource(R.drawable.play);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updatePlayButtonImage();
	}

	private void postButtonUpdateThread() {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				updatePlayButtonImage();
			}
		}, 234);
	}
	
	public void onMediaButtonClicked(View v) {
		String command = "";
		switch (v.getId()) {
		case R.id.prevButton:
			command = CMDPREVIOUS;
			break;
		case R.id.playPauseButton:
			command = CMDTOGGLEPAUSE;
			postButtonUpdateThread();
			break;
		case R.id.stopButton:
			command = CMDSTOP;
			postButtonUpdateThread();
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