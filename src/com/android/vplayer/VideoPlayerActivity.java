package com.android.vplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vplayer.domain.Video;
import com.android.vplayer.util.FormatUtil;
import com.android.vplayer.util.WindowUtils;

public class VideoPlayerActivity extends Activity implements SurfaceHolder.Callback,OnSeekBarChangeListener,OnTouchListener {
    private Button play;
    private Button showMode;
    private MediaPlayer mediaPlayer; 
    private SurfaceView surfaceView;
    private SeekBar seekBar;
    private TextView currentTime;
    private TextView totalTime;
    private TextView title;
    private TextView seekToPoint;
    private VideoControllLayout videoControllLayout;
    private ImageButton centerPlay;
    private int currentPoint = 0;
    private int maxTime;
    private Video video;
    private boolean isPlaying = false;
    private boolean isFullScreen = false;
    private boolean isShow = true;
    private boolean isFirstPlay = true;
    private MyServiceReceiver myReceiver;
    private int viewWidth;
    private int viewHeight;
    private Handler mHandler = new Handler();
    private Runnable hideControllRunnable = new Runnable() {
        @Override
        public void run() {
        	hideControll();
        }

    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_player);
		initActivity();
		addListener();
		mHandler.postDelayed(hideControllRunnable, 7000);
		
		myReceiver = new MyServiceReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("UPDATE_UI");
		registerReceiver(myReceiver, filter);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(mediaPlayer != null){
			mediaPlayer.reset();
		}
		super.onDestroy();
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(isFirstPlay){
		    play(currentPoint);
		    isFirstPlay =false;
		}
	}
	
	
	public void initActivity(){
		title = (TextView)findViewById(R.id.tv_title);
		centerPlay = (ImageButton)findViewById(R.id.bt_center_play);
		videoControllLayout = (VideoControllLayout)findViewById(R.id.controllView);
		play = videoControllLayout.getPlay();
		showMode = videoControllLayout.getShowMode();
		seekBar = videoControllLayout.getSeekBar();
		currentTime = videoControllLayout.getCurrentTime();
		totalTime = videoControllLayout.getTotalTime();
		seekToPoint = (TextView)findViewById(R.id.seekbar_indicator);
		
		Intent intent = getIntent();
		video = (Video)intent.getSerializableExtra("video");
		title.setText(video.getDisplayName());
		
		
		surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
		
		surfaceView.getHolder().addCallback(this);
		
		
		initMediaPlayer();
		
		
	}
	
	public void addListener(){
        seekBar.setOnSeekBarChangeListener(this);
        surfaceView.setOnTouchListener(this);
        videoControllLayout.setOnTouchListener(this);
        title.setOnTouchListener(this);
        play.setOnTouchListener(this);
        showMode.setOnTouchListener(this);
        centerPlay.setOnTouchListener(this);
	}

	public void initMediaPlayer(){
		try {
	         mediaPlayer = new MediaPlayer();
	         Uri myUri = Uri.parse(video.getPath().toString().trim());
		     //SurfaceHolder为显示影片的容器
		     mediaPlayer.setDataSource(this, myUri);
			 mediaPlayer.prepareAsync();
			 mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					public void onCompletion(MediaPlayer mp) {
						currentPoint = 0;
						isPlaying = false;
						play.setBackgroundResource(R.drawable.play);
					}
				});
				//当音频文件损坏时的回调函数
			mediaPlayer.setOnErrorListener(new OnErrorListener() {
					
					public boolean onError(MediaPlayer mp, int what, int extra) {
						isPlaying = false;
						currentPoint = 0;
						play.setBackgroundResource(R.drawable.play);
						return false;
					}
			   });
			
			 mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
					@Override
					public void onPrepared(MediaPlayer mp) {
						// TODO Auto-generated method stub
						maxTime = (int) mediaPlayer.getDuration();
						seekBar.setMax((int)maxTime);
						totalTime.setText(FormatUtil.formatTime(maxTime));
						currentTime.setText(FormatUtil.formatTime(currentPoint));
						viewWidth = mediaPlayer.getVideoWidth();
					    viewHeight = mediaPlayer.getVideoHeight();
					    setShowMode(viewWidth,viewHeight);
					}
				});
			 }catch (Exception e) {
					Toast.makeText(this, "播放器初始化异常", Toast.LENGTH_SHORT).show();
			}
	}
	
	public void play(final int current){
		mediaPlayer.setDisplay(surfaceView.getHolder());
		mediaPlayer.seekTo(current);
		currentTime.setText(FormatUtil.formatTime(current));
		mediaPlayer.start();
		play.setBackgroundResource(R.drawable.pause);
		centerPlay.setVisibility(View.GONE);
		new Thread(){
			public void run() {
				isPlaying = true;
				Intent intent = new Intent();
				intent.setAction("UPDATE_UI");
				while(isPlaying){
					currentPoint = mediaPlayer.getCurrentPosition();
					intent.putExtra("currentPoint", currentPoint);
					sendBroadcast(intent);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	public void pause(){
		if(mediaPlayer != null && mediaPlayer.isPlaying()){
			mediaPlayer.pause();
			currentPoint = mediaPlayer.getCurrentPosition();
			isPlaying = false;
		    play.setBackgroundResource(R.drawable.play);
		    Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
		    centerPlay.startAnimation(anim);
		    centerPlay.setVisibility(View.VISIBLE);
		}
	}
	
	public void setShowMode(int width,int height){
		int deviceWidth;
	    int deviceHeight;
	    int left;
	    int top;
	    LayoutParams param;
	    float scale = (float)width / (float)height;
		if(height > width){
			VideoPlayerActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			deviceWidth = WindowUtils.getWidth(this);
			deviceHeight = WindowUtils.getHeight(this);
			float deviceScale = (float)deviceWidth / (float)deviceHeight;
		    if(height < deviceHeight && width < deviceWidth){
		    	left = (deviceWidth - width)/2;
		    	top = (deviceHeight - height)/2;
		    	param = new LayoutParams(width, height);
		    	param.leftMargin = left;
		    	param.topMargin = top;
		    	surfaceView.setLayoutParams(param);
		    }else if(scale < deviceScale){
		    	left = (deviceWidth - (int)(deviceHeight*scale))/2;
		    	param = new LayoutParams((int)(deviceHeight*scale), (int)deviceHeight);
		    	param.leftMargin = left;
		    	surfaceView.setLayoutParams(param);
		    }else if(scale >= deviceScale){
		    	top = (deviceHeight - (int)(deviceWidth/scale))/2;
		    	param = new LayoutParams((int)deviceWidth, (int)(deviceWidth/scale));
		    	param.topMargin = top;
		    	surfaceView.setLayoutParams(param);
		    }
		}else{
			VideoPlayerActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			deviceWidth = WindowUtils.getWidth(this);
			deviceHeight = WindowUtils.getHeight(this);
			float deviceScale = (float)deviceWidth / (float)deviceHeight;
		    if(height < deviceHeight && width < deviceWidth){
		    	left = (deviceWidth - width)/2;
		    	top = (deviceHeight - height)/2;
		    	param = new LayoutParams(width, height);
		    	param.leftMargin = left;
		    	param.topMargin = top;
		    	surfaceView.setLayoutParams(param);
		    }else if(scale < deviceScale){
		    	left = (deviceWidth - (int)(deviceHeight*scale))/2;
		    	param = new LayoutParams((int)(deviceHeight*scale), (int)deviceHeight);
		    	param.leftMargin = left;
		    	surfaceView.setLayoutParams(param);
		    }else if(scale >= deviceScale){
		    	top = (deviceHeight - (int)(deviceWidth/scale))/2;
		    	param = new LayoutParams((int)deviceWidth, (int)(deviceWidth/scale));
		    	param.topMargin = top;
		    	surfaceView.setLayoutParams(param);
		    }
		}
	}

	public void changeShowMode(int width,int height){
		int deviceWidth;
	    int deviceHeight;
	    int left;
	    int top;
	    LayoutParams param;
		if(!isFullScreen){
			deviceWidth = WindowUtils.getWidth(this);
			deviceHeight = WindowUtils.getHeight(this);
			surfaceView.setLayoutParams(new LayoutParams(deviceWidth, deviceHeight));
			showMode.setBackgroundResource(R.drawable.no_full_screen);
			isFullScreen = true;
		}else{
			deviceWidth = WindowUtils.getWidth(this);
			deviceHeight = WindowUtils.getHeight(this);
			float scale = (float)width / (float)height;
			float deviceScale = (float)deviceWidth / (float)deviceHeight;
		    if(height < deviceHeight && width < deviceWidth){
		    	left = (deviceWidth - width)/2;
		    	top = (deviceHeight - height)/2;
		    	param = new LayoutParams(width, height);
		    	param.leftMargin = left;
		    	param.topMargin = top;
		    	surfaceView.setLayoutParams(param);
		    }else if(scale < deviceScale){
		    	left = (deviceWidth - (int)(deviceHeight*scale))/2;
		    	param = new LayoutParams((int)(deviceHeight*scale), (int)deviceHeight);
		    	param.leftMargin = left;
		    	surfaceView.setLayoutParams(param);
		    }else if(scale >= deviceScale){
		    	top = (deviceHeight - (int)(deviceWidth/scale))/2;
		    	param = new LayoutParams((int)deviceWidth, (int)(deviceWidth/scale));
		    	param.topMargin = top;
		    	surfaceView.setLayoutParams(param);
		    }
		    showMode.setBackgroundResource(R.drawable.full_screen);
		    isFullScreen = false;
		}
	}
	
	public void hideControll(){
		if(isShow){
		  Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.trans_out_bottom);
		  videoControllLayout.startAnimation(anim1);
		  videoControllLayout.setVisibility(View.GONE);
		  Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.trans_out_top);
		  title.startAnimation(anim2);
		  title.setVisibility(View.GONE);
		  isShow = false;
		}
	}
	
	public void showControll(){
		if(!isShow){
		  Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.trans_in_bottom);
		  videoControllLayout.startAnimation(anim1);
		  videoControllLayout.setVisibility(View.VISIBLE);
		  Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.trans_in_top);
		  title.startAnimation(anim2);
		  title.setVisibility(View.VISIBLE);
		  isShow = true;
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		 int id = v.getId();
		 switch (event.getAction()) {
         case MotionEvent.ACTION_DOWN:
        	 mHandler.removeCallbacks(hideControllRunnable);
             break;
         case MotionEvent.ACTION_MOVE:
             break;
         case MotionEvent.ACTION_UP:
        	 switch (id) {
			   case R.id.surfaceView:
				 if(v.getId() == R.id.surfaceView ){
	        	     int eventY = (int)event.getY();
	        	     int viewY = v.getHeight();
	        	     if(eventY > viewY/3*2){
	        	    	 showControll();
	        	     }else if(isPlaying){
	        			 pause();
	            	 }
	        	 }
				 break;
			   case R.id.bt_play:
					if(!isPlaying){
					   play(currentPoint);
					}else{
					   pause();
					}
					break;
				case R.id.bt_center_play:
					if(!isPlaying)
					   play(currentPoint);
					break;
				case R.id.bt_showmode:
					changeShowMode(viewWidth, viewHeight);
					break;
			    default:
				    break;
			 }
        	 mHandler.postDelayed(hideControllRunnable, 10000);
             break;
         default :
        	 break;
		 }
		return true;
	}
	
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		if(mediaPlayer != null && isPlaying){
	    	mediaPlayer.pause();
		}
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		if(mediaPlayer != null && isPlaying){
			mediaPlayer.setDisplay(surfaceView.getHolder());
			mediaPlayer.seekTo(currentPoint);
			mediaPlayer.start();
		}else if(mediaPlayer != null && !isPlaying){
			mediaPlayer.setDisplay(surfaceView.getHolder());
			mediaPlayer.seekTo(currentPoint);
		}
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}


	public void onStopTrackingTouch(SeekBar seekBar) {
		if(mediaPlayer != null){
			  mediaPlayer.seekTo(seekBar.getProgress());
	    }
		seekToPoint.setVisibility(View.GONE);
		mHandler.postDelayed(hideControllRunnable, 10000);
	}
	
	public void onStartTrackingTouch(SeekBar seekBar) {
		mHandler.removeCallbacks(hideControllRunnable);
		seekToPoint.setVisibility(View.VISIBLE);
	}
	
	@SuppressLint("NewApi")
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		seekToPoint.setText(FormatUtil.formatTime(progress)); 
	}
	
	
	public class MyServiceReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int point = (Integer)intent.getIntExtra("currentPoint", 0);
			seekBar.setProgress(point);
			currentTime.setText(FormatUtil.formatTime(point));
		}
	}


}
