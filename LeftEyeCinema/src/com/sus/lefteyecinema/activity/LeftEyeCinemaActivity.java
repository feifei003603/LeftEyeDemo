
package com.sus.lefteyecinema.activity;

import android.view.ViewTreeObserver;
import android.view.Window;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.sus.lefteyecinema.R;
import com.sus.lefteyecinema.adapter.LeftEyeAdapter;
import com.sus.lefteyecinema.bean.LeftEyeItems;
import com.sus.lefteyecinema.manager.NetModeStatusManager;
import com.sus.lefteyecinema.manager.NetModeStatusManager.OnNetModeChangeListener;
import com.sus.lefteyecinema.thread.LeftEyeLoadThread;
import com.sus.lefteyecinema.utils.DisplayImageOptionsUtil;
import com.sus.lefteyecinema.utils.NetUtils;
import com.sus.lefteyecinema.utils.OnTipsListener;
import com.sus.lefteyecinema.utils.UrlContainer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter.LengthFilter;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.io.File;
import java.lang.ref.WeakReference;

public class LeftEyeCinemaActivity extends Activity implements OnClickListener, OnItemClickListener,
        ViewTreeObserver.OnGlobalLayoutListener, OnTipsListener {
    public static final int MSG_ID_LOADING_SUCCESS = 2001;

    public static final int MSG_ID_LOADING_FAILED = 2002;

    private ImageView backView;

    private GridView gridView;

    private LeftEyeLoadThread loadThread;

    private Handler handler;

    private LeftEyeItems items;

    private LeftEyeAdapter mAdapter;

    private int mImageThumbWidth;

    private int mImageThumbSpacing;

    private double imgWidthParam = 0.5625;

    private int screenWidth;

    private int mOrgImageThumbWidth;

    private int screenHeight;

    private NetModeStatusManager netModeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_left_eye_cinema);
        handler = new MyHandler(this);
        backView = (ImageView) findViewById(R.id.activity_left_eye_cinema_back);
        gridView = (GridView) findViewById(R.id.activity_left_eye_cinema_gridview);
        mOrgImageThumbWidth = getResources().getDimensionPixelSize(R.dimen.web_img_width);
        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.web_img_spacing);
        backView.setOnClickListener(this);
        gridView.setOnItemClickListener(this);
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mAdapter = new LeftEyeAdapter(this);
        mAdapter.setImgWidthParam(imgWidthParam);
        mAdapter.setNumColumns(0);
        gridView.setAdapter(mAdapter);
        gridView.setOnScrollListener(DisplayImageOptionsUtil.getPauseOnScrollListener());
        setImgWidth();
        ViewStub zeroFlowViewStub = (ViewStub) findViewById(R.id.viewstub_left_eye_cinema_no_flow);
        netModeManager = new NetModeStatusManager(this, new MyNetModeStatusListener(),
                zeroFlowViewStub);
        initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadThread != null) {
            loadThread.interrupt();
            loadThread = null;
        }

        if (netModeManager != null) {
            netModeManager.unRegister();
            netModeManager = null;
        }

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finishActivity();
                break;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    private void initData() {
        if (NetUtils.isNetworkAvaliable(this)) {

            switchTargetView(1, OnTipsListener.TIPS_NONE);
            startLoadingThread(UrlContainer.LEFT_EYE_CINEMA);

        } else {
            loadingFail();
        }

    }

    /**
     * 开始请求数据的方法,要指定排序类型
     * 
     * @param pageIndex 当前页在pageView 的index
     * @param offset 请求数据偏移量;
     * @param sortIndex 排序种类 0:最热: 1:最细 ; 2:分数
     */
    private void startLoadingThread(String url) {
        if (loadThread != null && loadThread.isAlive()) {
            loadThread.interrupt();
            loadThread = null;
        }
        loadThread = new LeftEyeLoadThread(this, handler, url);
        loadThread.start();

    }

    /**
     * Handler的处理事件
     */
    private static class MyHandler extends Handler {
        WeakReference<LeftEyeCinemaActivity> thisLayout;

        MyHandler(LeftEyeCinemaActivity layout) {
            thisLayout = new WeakReference<LeftEyeCinemaActivity>(layout);
        }

        @Override
        public void handleMessage(Message msg) {
            final LeftEyeCinemaActivity theLayout = thisLayout.get();
            if (theLayout == null) {
                return;
            }

            switch (msg.what) {
                case MSG_ID_LOADING_SUCCESS:
                    theLayout.loadingSuccess((LeftEyeItems) msg.obj);
                    break;
                case MSG_ID_LOADING_FAILED:
                    theLayout.loadingFail();
                    break;

                default:
                    break;
            }
        }
    }

    private void loadingSuccess(LeftEyeItems items) {
        if (items == null) {
            loadingFail();
            return;
        }

        this.items = items;
        mAdapter.setShowItems(items);
        switchTargetView(0, OnTipsListener.TIPS_NONE);

    }

    private void loadingFail() {
        switchTargetView(2, OnTipsListener.TIPS_NO_NETWORK);
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onNoData() {

    }

    @Override
    public void onNoNetWork() {

    }

    /**
     * 切换显示页面
     * 
     * @param pageType
     * @param exceptionType
     */
    private void switchTargetView(int pageType, int exceptionType) {
        View loadingView = (View) findViewById(R.id.viewstub_inflate_left_eye_cinema_loading);
        View tipsView = (View) findViewById(R.id.viewstub_inflate_left_eye_cinema_tips);

        switch (pageType) {
            case 0: {
                if (tipsView != null) {
                    tipsView.setVisibility(View.INVISIBLE);
                }
                if (loadingView != null) {
                    loadingView.setVisibility(View.INVISIBLE);
                }
                if (netModeManager != null) {
                    netModeManager.dismissZeroFlowView();
                }
                break;
            }
            case 1: {
                // 加载页面
                View view = inflateSubView(R.id.viewstub_left_eye_cinema_loading,
                        R.id.viewstub_inflate_left_eye_cinema_loading);
                if (view != null) {
                    String[] list = getResources().getStringArray(R.array.common_loading_text);
                    int index = (int) (Math.random() * list.length);
                    TextView txt = (TextView) view.findViewById(R.id.lay_progressbar_text);
                    txt.setText(list[index]);
                    view.setVisibility(View.VISIBLE);
                }
                if (tipsView != null) {
                    tipsView.setVisibility(View.INVISIBLE);
                }
                if (netModeManager != null) {
                    netModeManager.dismissZeroFlowView();
                }
                break;
            }
            case 2: {
                // 异常页面
                View view = inflateExceptionSubView(R.id.viewstub_left_eye_cinema_tips,
                        R.id.viewstub_inflate_left_eye_cinema_tips, exceptionType, this);
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
                if (loadingView != null) {
                    loadingView.setVisibility(View.INVISIBLE);
                }
                if (netModeManager != null) {
                    netModeManager.dismissZeroFlowView();
                }
                break;
            }
            case 3: {
                // 零流量页面
                if (netModeManager != null) {
                    netModeManager.showZeroFlowView();
                }
                if (loadingView != null) {
                    loadingView.setVisibility(View.INVISIBLE);
                }
                if (tipsView != null) {
                    tipsView.setVisibility(View.GONE);
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onGlobalLayout() {
        if (mAdapter != null && mAdapter.getNumColumns() <= 0) {
            getScreenWidth();
            final int numColumns = (int) Math
                    .floor(screenWidth / (mImageThumbWidth + mImageThumbSpacing));
            int columnWidth; 
            if (numColumns > 0) {
                columnWidth= (screenWidth - (numColumns + 1) * mImageThumbSpacing) / numColumns;
                mAdapter.setNumColumns(numColumns);
            } else {
                columnWidth = screenWidth - 2 * mImageThumbSpacing;
                mAdapter.setNumColumns(1);

            }
            mAdapter.setItemHeight(columnWidth);
            gridView.setColumnWidth(columnWidth);
        }
    }

    private void setImgWidth() {
        imgWidthParam = 0.5625;
        mImageThumbWidth = mOrgImageThumbWidth;
    }

    private void getScreenWidth() {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(dm);
            screenWidth = dm.widthPixels;
            // screenHeight = dm.heightPixels;

            if (screenWidth <= 0) {
                screenWidth = 480;
            }
        } catch (Exception e) {
            screenWidth = 480;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "click position " + position, Toast.LENGTH_SHORT)
                .show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_left_eye_cinema_back:
                finishActivity();
                break;

        }
    }

    class MyNetModeStatusListener implements OnNetModeChangeListener {

        @Override
        public void onUpdateData() {
            switchTargetView(0, OnTipsListener.TIPS_NONE);
            initData();
        }

        @Override
        public void onShowNoNetView() {
            switchTargetView(2, OnTipsListener.TIPS_NO_NETWORK);
        }

        @Override
        public void onShowNetModeView() {
            switchTargetView(3, OnTipsListener.TIPS_NONE);
        }

        @Override
        public void onHideNetModeView() {
            initData();
        }
    }

    public void finishActivity() {
        finish();
        // overridePendingTransition(R.anim.push_right_in,
        // R.anim.push_right_out);
    }

    protected View inflateSubView(int subId, int inflateId) {
        View noNetSubTree = findViewById(inflateId);
        if (noNetSubTree == null) {
            ViewStub viewStub = (ViewStub) findViewById(subId);
            noNetSubTree = viewStub.inflate();
        }
        noNetSubTree.setVisibility(View.VISIBLE);
        return noNetSubTree;
    }

    /**
     * 延迟加载异常信息提示页面
     * 
     * @param subId
     * @param inflateId
     * @param type
     * @param listener
     * @return
     */
    protected View inflateExceptionSubView(int subId, int inflateId, final int type,
            final OnTipsListener listener) {
        View noNetSubTree = (View) findViewById(inflateId);
        if (noNetSubTree == null) {
            ViewStub viewStub = (ViewStub) findViewById(subId);
            noNetSubTree = viewStub.inflate();
        }
        // 初始化
        TextView tipText = (TextView) findViewById(R.id.saying_bg_textview);
        Button confirmBtn = (Button) findViewById(R.id.saying_refresh_btn);
        ImageView iconImage = (ImageView) findViewById(R.id.saying_bg_imageview);
        switch (type) {
            case OnTipsListener.TIPS_NO_NETWORK: {
                tipText.setText("网络不能用鸟 (⊙﹏⊙)~");
                confirmBtn.setText("刷新");
                iconImage.setImageResource(R.drawable.nodata);
                break;
            }
            case OnTipsListener.TIPS_NO_DATA: {
                tipText.setText("糟糕！好像出错了");
                confirmBtn.setText("刷新");
                iconImage.setImageResource(R.drawable.nodata);
                break;
            }
            case OnTipsListener.TIPS_SERVER_UPDATE: {
                tipText.setText("服务器升级中~");
                confirmBtn.setText("去本地视频看看");
                iconImage.setImageResource(R.drawable.nodata);
                break;
            }
            default:
                break;

        }
        View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (listener == null) {
                    return;
                }
                if (type == OnTipsListener.TIPS_NO_NETWORK) {
                    listener.onNoNetWork();
                } else if (type == OnTipsListener.TIPS_NO_DATA) {
                    listener.onNoData();
                } else if (type == OnTipsListener.TIPS_SERVER_UPDATE) {
                    listener.onUpdate();
                }
            }

        };
        if (listener != null) {
            confirmBtn.setOnClickListener(clickListener);
        }
        noNetSubTree.setVisibility(View.VISIBLE);
        return noNetSubTree;
    }

}
