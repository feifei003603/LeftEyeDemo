
package com.sus.lefteyecinema.application;


import com.sus.lefteyecinema.utils.ImageUtil;

import android.app.Application;

public class LeftEyeApplication extends Application {
    private static LeftEyeApplication instance;

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        ImageUtil.initImageLoader(this);
    }
    
    public static LeftEyeApplication getInstance() {
        return instance;
    }

}
