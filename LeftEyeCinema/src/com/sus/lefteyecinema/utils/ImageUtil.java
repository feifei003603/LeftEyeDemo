
package com.sus.lefteyecinema.utils;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.content.Context;

import java.io.File;

public class ImageUtil {
    /**
     * 初始化图片加载相关
     * 
     * @param context
     */
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(context);
        // int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // // 使用最大可用内存值的1/8作为缓存的大小。
        // int cacheSize = maxMemory / 8;

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                // max width, max height，即保存的每个缓存文件的最大长宽
                .diskCacheExtraOptions(720, 1280, null).threadPriority(Thread.NORM_PRIORITY - 2)
                .diskCache(new UnlimitedDiscCache(cacheDir)).denyCacheImageMultipleSizesInMemory()
                .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(1000)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).memoryCacheSizePercentage(15)
                .build();
        ImageLoader.getInstance().init(config);
    }

}
