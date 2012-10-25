package com.san.mediaplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MediaPlayerTweakActivity extends Activity {
	private Button prevButton, stopButton, nextButton;
	private ToggleButton playPauseButton;
	private GestureDetector mDetector;
	private LinearLayout layout;
	private static final String TAG = "MediaPlayerTweakActivity";

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
						| LayoutParams.FLAG_TURN_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		mDetector = new GestureDetector(this, new MyGestureListener());

		prevButton = (Button) findViewById(R.id.prevButton);
		playPauseButton = (ToggleButton) findViewById(R.id.playPauseButton);
		stopButton = (Button) findViewById(R.id.stopButton);
		nextButton = (Button) findViewById(R.id.nextButton);
		layout = (LinearLayout) findViewById(R.id.layout);
		layout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return mDetector.onTouchEvent(event);
			}
		});

		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		mDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
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

		sendCommand(command);
	}

	private void sendCommand(String command) {
		Intent intent = new Intent(SERVICECMD);
		intent.putExtra(CMDNAME, command);
		sendBroadcast(intent);
	}

	class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

		private static final int SWIPE_MIN_DISTANCE = 150;

		private static final int SWIPE_MAX_OFF_PATH = 100;

		private static final int SWIPE_THRESHOLD_VELOCITY = 100;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,

		float velocityY) {

			float dX = e2.getX() - e1.getX();

			float dY = e1.getY() - e2.getY();

			if (Math.abs(dY) < SWIPE_MAX_OFF_PATH &&

			Math.abs(velocityX) >= SWIPE_THRESHOLD_VELOCITY &&

			Math.abs(dX) >= SWIPE_MIN_DISTANCE) {

				if (dX > 0) {
					Log.d(TAG, "right swipe");
					Toast.makeText(getApplicationContext(), CMDNEXT,
							Toast.LENGTH_SHORT).show();
					sendCommand(CMDNEXT);

				} else {

					Toast.makeText(getApplicationContext(), CMDPREVIOUS,
							Toast.LENGTH_SHORT).show();
					sendCommand(CMDPREVIOUS);

				}

				return true;

			} else if (Math.abs(dX) < SWIPE_MAX_OFF_PATH &&

			Math.abs(velocityY) >= SWIPE_THRESHOLD_VELOCITY &&

			Math.abs(dY) >= SWIPE_MIN_DISTANCE) {

				if (dY > 0) {

					Toast.makeText(getApplicationContext(), CMDTOGGLEPAUSE,
							Toast.LENGTH_SHORT).show();
					sendCommand(CMDTOGGLEPAUSE);

				} else {

					Toast.makeText(getApplicationContext(), CMDSTOP,
							Toast.LENGTH_SHORT).show();
					sendCommand(CMDSTOP);
				}

				return true;

			}

			return false;

		}
	}
}