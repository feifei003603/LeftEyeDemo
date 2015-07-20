/**
 * 功能：传输状态栏管理类，主要负责状态的一切显示
 */

package com.sus.lefteyecinema.manager;

import com.sus.lefteyecinema.R;
import com.sus.lefteyecinema.utils.NetUtils;

/*import com.storm.smart.R;
import com.storm.smart.activity.ThriftConfigActivity;
import com.storm.smart.common.utils.LogHelper;
import com.storm.smart.db.Preferences;
import com.storm.smart.netflow.FlowServiceImpl;
import com.storm.smart.utils.NetUtils;*/

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 功能：传输状态栏管理类，主要负责状态的一切显示
 */
public class NetModeStatusManager {
    private static final String TAG = "NetModeStatusManager";

    // 上下文Context
    private Activity mContext;

    private NetWorkReceiver mNetWorkReceiver;

    private NetModeChangedReceiver netModeReceiver;

    private OnNetModeChangeListener changeListener;

    private ViewGroup rootView;

    private ViewStub mViewStub;

    View noFlowModeView;

    /**
     * 功能：构造函数
     */
    public NetModeStatusManager(Activity argContext, OnNetModeChangeListener changeListener,
            ViewGroup rootView) {
        this.mContext = argContext;
        this.changeListener = changeListener;
        this.rootView = rootView;
        register();
    }

    public NetModeStatusManager(Activity argContext, OnNetModeChangeListener changeListener,
            ViewStub viewStub) {
        this.mContext = argContext;
        this.changeListener = changeListener;
        mViewStub = viewStub;
        register();
    }

    public void showZeroFlowView() {
        if (rootView != null) {
            noFlowModeView = LayoutInflater.from(mContext).inflate(R.layout.zero_flow_view, null);
            rootView.addView(noFlowModeView);
            initView(rootView);
        } else if (mViewStub != null) {
            if (noFlowModeView == null) {
                noFlowModeView = mViewStub.inflate();
                initView(noFlowModeView);
            }
            noFlowModeView.setVisibility(View.VISIBLE);
        }
    }

    public void dismissZeroFlowView() {
        if (rootView != null) {
            rootView.removeView(noFlowModeView);
        } else if (noFlowModeView != null) {
            noFlowModeView.setVisibility(View.GONE);
        }
    }

    private void initView(View view) {
    }

    /**
     * 功能：刷新当前网络模式
     */

    public void updateNetModeBanner() {
        if (NetUtils.isNetworkAvaliable(mContext)) {
            if (NetUtils.isWifiConnected(mContext)) {// wifi连接
                if (changeListener != null) {
                    changeListener.onHideNetModeView();
                }
            } else {
                if (changeListener != null) {
                    changeListener.onHideNetModeView();
                }
            }

        } else {
            if (changeListener != null) {
                changeListener.onShowNoNetView();
            }
        }
    }

    private String lastConnectType = "";

    /**
     * 网络切换
     * 
     */
    private class NetWorkReceiver extends BroadcastReceiver {
        boolean isFirst = true;

        public void onReceive(Context context, Intent intent) {
            if (isFirst) {
                isFirst = false;
                return;
            }
            String action = intent.getAction();

            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager connMgr = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                boolean isWifiConn = networkInfo.isConnected();
                if (isWifiConn) {
                    if (!"WI_FI".equals(lastConnectType)) {
                        if (changeListener != null) {
                            changeListener.onUpdateData();
                        }
                    } else {
                    }
                    lastConnectType = "WI_FI";
                } else {
                    networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    boolean isMobileConn = false;
                    if (networkInfo != null && networkInfo.isConnected()) {
                        isMobileConn = true;
                    }
                    if (isMobileConn) {
                        if (!"Mobile".equals(lastConnectType)) {
                            updateNetModeBanner();
                        }
                        lastConnectType = "Mobile";
                    } else {
                        if (!"ANY".equals(lastConnectType)) {
                            updateNetModeBanner();
                        }
                        lastConnectType = "ANY";
                    }
                }
            }
        }
    }

    /**
     * 手动切换
     * 
     */
    private class NetModeChangedReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(NetUtils.netModeChangedAction)) {
                if (changeListener != null) {
                    changeListener.onUpdateData();
                }

            }
        }
    }

    private void register() {
        mNetWorkReceiver = new NetWorkReceiver();
        IntentFilter netFilter = new IntentFilter();
        netFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(mNetWorkReceiver, netFilter);

        netModeReceiver = new NetModeChangedReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NetUtils.netModeChangedAction);
        mContext.registerReceiver(netModeReceiver, intentFilter);

    }

    public void unRegister() {
        if (mNetWorkReceiver != null) {
            mContext.unregisterReceiver(mNetWorkReceiver);
        }

        if (netModeReceiver != null) {
            mContext.unregisterReceiver(netModeReceiver);
        }

        changeListener = null;
    }

    /**
     */
    public interface OnNetModeChangeListener {

        public void onShowNetModeView();

        public void onHideNetModeView();

        public void onShowNoNetView();

        public void onUpdateData();
    }
}
