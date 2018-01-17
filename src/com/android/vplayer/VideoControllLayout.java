package com.android.vplayer;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class VideoControllLayout extends LinearLayout{
	private Button play;
    private Button showMode;
    private SeekBar seekBar;
    private TextView currentTime;
    private TextView totalTime;
	public VideoControllLayout(Context context) {
		super(context);
	}
	
	public VideoControllLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.player_controll,
				this, true);
		    play = (Button)findViewById(R.id.bt_play);
			showMode = (Button)findViewById(R.id.bt_showmode);
			seekBar = (SeekBar)findViewById(R.id.seekBar);
			currentTime = (TextView)findViewById(R.id.currentTime);
			totalTime = (TextView)findViewById(R.id.totalTime);
	}
	public Button getPlay() {
		return play;
	}
	public void setPlay(Button play) {
		this.play = play;
	}
	public Button getShowMode() {
		return showMode;
	}
	public void setShowMode(Button showMode) {
		this.showMode = showMode;
	}
	public SeekBar getSeekBar() {
		return seekBar;
	}
	public void setSeekBar(SeekBar seekBar) {
		this.seekBar = seekBar;
	}
	public TextView getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(TextView currentTime) {
		this.currentTime = currentTime;
	}
	public TextView getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(TextView totalTime) {
		this.totalTime = totalTime;
	}
	
}
