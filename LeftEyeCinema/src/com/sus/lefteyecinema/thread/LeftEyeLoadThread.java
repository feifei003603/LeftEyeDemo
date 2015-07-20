
package com.sus.lefteyecinema.thread;

import com.sus.lefteyecinema.activity.LeftEyeCinemaActivity;
import com.sus.lefteyecinema.bean.LeftEyeItem;
import com.sus.lefteyecinema.bean.LeftEyeItems;
import com.sus.lefteyecinema.utils.CustomException;
import com.sus.lefteyecinema.utils.HandlerMsgUtils;
import com.sus.lefteyecinema.utils.NetUtils;

/*import com.storm.smart.activity.LeftEyeCinemaActivity;
import com.storm.smart.common.exception.CustomException;
import com.storm.smart.domain.LeftEyeItem;
import com.storm.smart.domain.LeftEyeItems;
import com.storm.smart.utils.HandlerMsgUtils;
import com.storm.smart.utils.NetUtils;*/

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketException;

public class LeftEyeLoadThread extends Thread {
    private Context context;

    private Handler handler;

    private String url;

    public static final int LOADING_NORMAL = 0;

    public static final int LOADING_FIRT = 1;

    public static final int LOADING_SORT = 2;

    public LeftEyeLoadThread(Context context, Handler handler, String url) {
        super();
        this.context = context;
        this.handler = handler;
        this.url = url;
    }

    @Override
    public void run() {
        LeftEyeItems showItems;
        try {
            showItems = doGet();
            if (showItems == null) {
                HandlerMsgUtils.sendMsg(handler, LeftEyeCinemaActivity.MSG_ID_LOADING_FAILED);
            } else {
                HandlerMsgUtils.sendMsg(handler, LeftEyeCinemaActivity.MSG_ID_LOADING_SUCCESS, showItems);
            }
        } catch (Exception e) {
            HandlerMsgUtils.sendMsg(handler, LeftEyeCinemaActivity.MSG_ID_LOADING_FAILED);
        }
    }

    private LeftEyeItems doGet() throws MalformedURLException, ProtocolException, SocketException,
            IOException, CustomException, JSONException {
        LeftEyeItems items = new LeftEyeItems();
        String jsonString = NetUtils.getJsonStringFrUrl(context, url);
        if (jsonString == null || "".equals(jsonString.trim()) || "[]".equals(jsonString.trim())) {
            throw new JSONException("josn is null");
        }

        JSONObject jsonObj;
        JSONArray result;
        String periodId;
        try {
            jsonObj = new JSONObject(jsonString);
            result = jsonObj.getJSONArray("result");
            for (int i = 0; i < result.length(); i++) {
                JSONObject obj1 = result.getJSONObject(i);
                periodId = obj1.getString("id");
                    JSONArray arr2 = obj1.getJSONArray("items");
                    for (int j = 0; j < arr2.length(); j++) {
                        LeftEyeItem item = new LeftEyeItem();
                        JSONObject obj3 = arr2.getJSONObject(j);
                        item.setperiodId(periodId);
                        item.setCover_url(obj3.getString("sub_cover_url"));
                        item.setDesc(obj3.getString("desc"));
                        item.setId(obj3.getString("id"));
                        item.setTitle(obj3.getString("title"));
                        item.setSite(obj3.getJSONObject("detail").getString("max_site"));
                        item.setThreeD(obj3.getJSONObject("detail").getString("3d"));
                        item.setHas(obj3.getJSONObject("detail").getString("has"));
                        items.addItem(item);
                    }
                }
        } catch (Exception e) {
            return null;
        }
        return items;
    }
}
