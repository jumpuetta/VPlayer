package com.android.vplayer.util;

import java.io.File;
import java.util.List;

import com.android.vplayer.domain.Video;

public class FileUtil {
	
	public static boolean usesExternalStorage() {  
		 if (android.os.Environment.getExternalStorageState().equals(  
			    android.os.Environment.MEDIA_MOUNTED)) {  
		          return true;  
			  } else  
			   return false;  
			 } 
	
	public static void doSearch(List<Video> videoList, String path) {
		File file = new File(path);
		if (file.isDirectory()) {	
			File[] fileArray = file.listFiles();
			if(fileArray != null){
			  for (File f : fileArray) {
				doSearch(videoList,f.getPath());
			  }
			}
		}else {
		    if(file.getName().endsWith(".mp4") || file.getName().endsWith(".rmvb")
				||file.getName().endsWith(".avi") || file.getName().endsWith(".3gp")
				|| file.getName().endsWith(".rm")|| file.getName().endsWith(".wmv")
				|| file.getName().endsWith(".mov")){
		       System.out.println(file.getName());
			   Video video = new Video();
			   video.setPath(file.getAbsolutePath());
			   video.setDisplayName(file.getName());
			   videoList.add(video);
		    }
		} 
	}
}
	