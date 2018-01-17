package com.android.vplayer;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;

import com.android.vplayer.dao.VideoProvider;
import com.android.vplayer.domain.Video;

public class VideoShowActivity extends Activity implements OnItemClickListener{
	private List<Video> videoList;
    private FancyCoverFlow fancyCoverFlow;
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_inflate);
        videoList = VideoProvider.getVideoListByCursor(this);
        fancyCoverFlow = (FancyCoverFlow) findViewById(R.id.fancyCoverFlow);;
        fancyCoverFlow.setAdapter(new VideoShowAdapter(videoList,this));
        fancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);
        fancyCoverFlow.setReflectionEnabled(true);
        fancyCoverFlow.setReflectionRatio(0.4f);
        fancyCoverFlow.setReflectionGap(0);
        
        fancyCoverFlow.setOnItemClickListener(this);
    }
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Video video= videoList.get(position);
		Intent intent = new Intent();
		intent.setClassName(this, "com.android.vplayer.VideoPlayerActivity");
		intent.putExtra("video", video);
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}




}
