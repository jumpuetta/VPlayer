package com.android.vplayer.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;



public class WindowUtils {

	private static int width;
	private static int height;

	public static int getWidth(Context context) {
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels;
		return width;
	}

	public static int getHeight(Context context) {
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(metric);
		height = metric.heightPixels;
		return height;
	}

}
