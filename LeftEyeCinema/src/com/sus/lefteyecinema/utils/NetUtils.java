
package com.sus.lefteyecinema.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.URL;


public class NetUtils {

    private static final String TAG = "NetUtils";

    // 网络模式
    public static final String netModeChangedAction = "NETMODECHANGED";

    public static final int normalNetState = 0; // 正常

    public static final int noFlowNetState = 2;// 0流量

    public static final int noPicNetState = 1; // 无图

    public static final int wifiNetState = 6;

    public static final int offLineState = 7;

    /** 网络连接超时时间 */
    public static final int SOCKET_TIME_OUT_TIME = 30000;

    /** 网络读取时间 */
    public static final int SOCKET_READ_TIME = 20000;

    public static int getNetWorkState(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return info.getType();
        }
        return -1;
    }
    
    public static boolean isNetworkAvaliable(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager cManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cManager == null) {
            return false;
        }
        NetworkInfo info = cManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
        }

        return false;
    }


    public static boolean is3GConnected(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) {
                return false;
            }
            NetworkInfo mobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobileInfo != null && mobileInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 联网获取数据的方法,增加失败重新连接方法
     *
     * @param context
     * @param uri
     * @return
     * @throws Exception
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws SocketException
     * @throws IOException
     * @throws CustomException
     * @throws BaiduFaildException
     */
    public static String getJsonStringFrUrl(final Context context, String uri)
            throws MalformedURLException, ProtocolException, SocketException, IOException,
            CustomException {

        // 添加重试策略，如果在3次都没有获得信息，提示错误

        String resultString = "";
        for (int i = 0; i < 3; i++) {
            try {
                resultString = getJsonStringFrUrl2(context, uri);
                break;
            } catch (CustomException e) {
                // 服务器正在升级
                if (e != null) {
                    throw e;
                }
            } catch (MalformedURLException e) {
                if (i == 2) {
                    throw e;
                }
            } catch (ProtocolException e) {
                if (i == 2) {
                    throw e;
                }
            } catch (SocketException e) {
                if (i == 2) {
                    throw e;
                }
            } catch (IOException e) {
                if (i == 2) {
                    throw e;
                }
            }
        }
        return resultString;
    }

    /**
     * 联网获取数据的方法
     *
     * @param uri
     * @return
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws SocketException
     * @throws CustomException
     * @throws IOException
     */
    private static String getJsonStringFrUrl2(Context context, String uri)
            throws MalformedURLException, ProtocolException, SocketException, CustomException,
            IOException {
        return doGet(context, uri, SOCKET_TIME_OUT_TIME, SOCKET_READ_TIME);
    }

    /**
     * 联网获取数据的方法
     *
     * @param uri
     * @return
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws SocketException
     * @throws CustomException
     * @throws IOException
     */
    public static String doGet(Context context, String uri, int socketTimeOut, int socket_read_time)
            throws MalformedURLException, ProtocolException, SocketException, CustomException,
            IOException {
        StringBuilder builder;
        InputStream inStream = null;
        HttpURLConnection conn = null;
        try {
            builder = new StringBuilder();
            URL url = new URL(uri);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(socketTimeOut);
            conn.setReadTimeout(socket_read_time);
            conn.setRequestMethod("GET");

            inStream = conn.getInputStream();
            byte[] buffer = new byte[1024 * 8];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                builder.append(new String(buffer, 0, len));
            }
        } finally {
            if (inStream != null) {
                inStream.close();
                inStream = null;
            }
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }

        return builder.toString();
    }
    
    public static boolean isWifiConnected(Context context) {
        if (context == null) {
            return false;
        }
        if (!isWiFiOpened(context)) {
            return false;
        }

        boolean isOpened = false;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        try {
            networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        } catch (Exception e) {
            return isOpened;
        }

        WifiManager wfManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (networkInfo != null && networkInfo.isConnected() && wfManager.pingSupplicant()) {
            isOpened = true;
        }

        return isOpened;
    }
    
    private static boolean isWiFiOpened(Context context) {
        WifiManager wfManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wfManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            return true;
        }

        return false;
    }

}
