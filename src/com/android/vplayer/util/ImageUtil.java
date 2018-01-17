package com.android.vplayer.util;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Images;

public class ImageUtil {
	public  static Bitmap createVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
          try {
        	  bitmap = ThumbnailUtils.createVideoThumbnail(filePath, Images.Thumbnails.MINI_KIND);
          } catch (Exception ex) {
        } finally {
        }
        return bitmap;
    }
}
