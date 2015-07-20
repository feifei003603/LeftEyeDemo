
package com.sus.lefteyecinema.utils;

/**
 * 异常提示监听
 * 
 */
public interface OnTipsListener {

    public static final int TIPS_NO_NETWORK = 0; // 网络异常

    public static final int TIPS_NO_DATA = 1; // 无数据

    // public static final int TIPS_ZERO_FLOW = 2; //零流量

    public static final int TIPS_SERVER_UPDATE = 3; // 服务器升级

    public static final int TIPS_NONE = 1000; // 无提示

    public void onUpdate();

    public void onNoData();

    //public void onZeroMode();
    
    public void onNoNetWork();
}
