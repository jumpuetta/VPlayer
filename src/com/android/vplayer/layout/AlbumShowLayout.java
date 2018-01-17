package com.android.vplayer.layout;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.vplayer.R;

public  class AlbumShowLayout extends RelativeLayout {

    private TextView textView;

    private ImageView imageView;

    public AlbumShowLayout(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.album_show,this, true);

        this.textView = (TextView)findViewById(R.id.tv_title);
        this.imageView = (ImageView)findViewById(R.id.img_album);


        this.textView.setGravity(Gravity.CENTER);
        this.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        this.imageView.setLayoutParams(new LayoutParams(600,400));
        this.imageView.setScaleType(ImageView.ScaleType.FIT_XY);

    }


    public TextView getTextView() {
        return textView;
    }

    public ImageView getImageView() {
        return imageView;
    }
}