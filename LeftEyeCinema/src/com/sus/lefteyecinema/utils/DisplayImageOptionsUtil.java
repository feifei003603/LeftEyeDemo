
package com.sus.lefteyecinema.utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import android.graphics.Bitmap;
import android.widget.AbsListView.OnScrollListener;

public class DisplayImageOptionsUtil {

    private static boolean memoryCache = true;

    public static DisplayImageOptions getOptions() {
        return new DisplayImageOptions.Builder().cacheInMemory(memoryCache).cacheOnDisk(true)
                .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
    }

    public static DisplayImageOptions getRoundOptions() {
        return new DisplayImageOptions.Builder().cacheInMemory(memoryCache).cacheOnDisk(true)
                .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).displayer(new RoundedBitmapDisplayer(4))
                .build();
    }

    public static DisplayImageOptions getOptions(int drawable) {
        return new DisplayImageOptions.Builder().showImageOnLoading(drawable)
                .showImageForEmptyUri(drawable).showImageOnFail(drawable)
                .cacheInMemory(memoryCache).cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY).build();
    }

    public static DisplayImageOptions getLogoBgOptions() {
        return new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)//
                .build();
    }

    public static DisplayImageOptions getLogoBgOptions(Bitmap.Config colorType) {
        return new DisplayImageOptions.Builder().bitmapConfig(colorType)//
                .build();
    }

    public static PauseOnScrollListener getPauseOnScrollListener() {
        PauseOnScrollListener listener = new PauseOnScrollListener(ImageLoader.getInstance(),
                false, false);
        return listener;
    }

    public static PauseOnScrollListener getPauseOnScrollListener(OnScrollListener scrollListener) {
        PauseOnScrollListener listener = new PauseOnScrollListener(ImageLoader.getInstance(),
                false, true, scrollListener);
        return listener;
    }

    public static DisplayImageOptions getGameCenterOptions(int drawable) {
        return new DisplayImageOptions.Builder().showImageForEmptyUri(drawable)
                .showImageOnFail(drawable).cacheInMemory(memoryCache).cacheOnDisk(true)
                .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
    }
}
