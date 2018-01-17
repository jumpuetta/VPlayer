package com.android.vplayer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import at.technikum.mti.fancycoverflow.FancyCoverFlowAdapter;

import com.android.vplayer.domain.Video;
import com.android.vplayer.layout.AlbumShowLayout;
import com.android.vplayer.util.ImageUtil;

public class VideoShowAdapter extends FancyCoverFlowAdapter {

    private List<Video> videoList ;
    private List<Bitmap> imageList;
    public VideoShowAdapter(List<Video> videoList,Context context){
    	this.videoList = videoList;
    	imageList = new ArrayList<Bitmap>();
    	for (Video video : videoList) {
    		imageList.add(ImageUtil.createVideoThumbnail(video.getPath()));
		}
    }
    
    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return videoList.get(position);
	} 
    
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getCoverFlowItem(int i, View reuseableView, ViewGroup viewGroup) {

    	AlbumShowLayout customViewGroup = null;

          if (reuseableView != null) {
              customViewGroup = (AlbumShowLayout) reuseableView;
          } else {
              customViewGroup = new AlbumShowLayout(viewGroup.getContext());
              customViewGroup.setLayoutParams(new FancyCoverFlow.LayoutParams(700, 520));
          }
          customViewGroup.getImageView().setImageBitmap(imageList.get(i));
          customViewGroup.getTextView().setText(videoList.get(i).getDisplayName());
    	  
    	  return customViewGroup;
    }

}
