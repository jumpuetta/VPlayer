package com.android.vplayer.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

import com.android.vplayer.domain.Video;
import com.android.vplayer.util.FileUtil;

public class VideoProvider {
	    
	public static List<Video> getVideoListByCursor(Context context) {
	        List<Video> list = new ArrayList<Video>();
	        if (context != null&&FileUtil.usesExternalStorage()) {
	            Cursor cursor = context.getContentResolver().query(
	                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
	                    null, null);
	            if (cursor != null) {
	                while (cursor.moveToNext()) {
	                    int id = cursor.getInt(cursor
	                            .getColumnIndexOrThrow(MediaStore.Video.Media._ID));
	                    String title = cursor
	                            .getString(cursor
	                                    .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
	                    String album = cursor
	                            .getString(cursor
	                                    .getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
	                    String artist = cursor
	                            .getString(cursor
	                                    .getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
	                    String displayName = cursor
	                            .getString(cursor
	                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
	                    String mimeType = cursor
	                            .getString(cursor
	                                    .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
	                    String path = cursor
	                            .getString(cursor
	                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
	                    long duration = cursor
	                            .getInt(cursor
	                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
	                    long size = cursor
	                            .getLong(cursor
	                                    .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
	                    Video video = new Video(id, title, album, artist, displayName, mimeType, path, size, duration);
	                    list.add(video);
	                }
	                cursor.close();
	            }
	        }
	        return list;
	    }
	
	public static List<Video> getVideoListByFilePath(){
		   List<Video> videoList = new ArrayList<Video>();
		   FileUtil.doSearch(videoList,Environment.getExternalStorageDirectory().toString()+"/");
		   return videoList;
	} 
}
