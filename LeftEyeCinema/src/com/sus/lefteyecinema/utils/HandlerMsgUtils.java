
package com.sus.lefteyecinema.utils;

import android.os.Handler;
import android.os.Message;

public class HandlerMsgUtils {

    public static void sendMsg(Handler handler, int what) {
        if(handler == null){
            return;
        }
        Message message = new Message();
        message.what = what;
        handler.sendMessage(message);
    }

    public static void sendMsg(Handler handler, int what, int arg1) {
        if(handler == null){
            return;
        }
        Message message = new Message();
        message.what = what;
        message.arg1 = arg1;
        handler.sendMessage(message);
    }

    public static void sendMsg(Handler handler, int what, int arg1, int arg2) {
        if(handler == null){
            return;
        }
        Message message = new Message();
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        handler.sendMessage(message);
    }

    public static void sendMsgDelay(Handler handler, int what, Object obj, long delay) {
        if(handler == null){
            return;
        }
        Message message = new Message();
        message.what = what;
        message.obj = obj;
        handler.sendMessageDelayed(message, delay);
    }

    public static void sendMsgDelay(Handler handler, int what, int arg1, long delay) {
        if(handler == null){
            return;
        }
        Message message = new Message();
        message.what = what;
        message.arg1 = arg1;
        handler.sendMessageDelayed(message, delay);
    }

    public static void sendMsg(Handler handler, int what, Object obj) {
        if(handler == null){
            return;
        }
        Message message = new Message();
        message.what = what;
        message.obj = obj;
        handler.sendMessage(message);
    }

    public static void sendMsg(Handler handler, int what, int arg1, int arg2, Object obj) {
        if(handler == null){
            return;
        }
        Message message = new Message();
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        message.obj = obj;
        handler.sendMessage(message);
    }

    public static void sendMsg(Handler handler, int what, int arg1, Object obj) {
        if(handler == null){
            return;
        }
        Message message = new Message();
        message.what = what;
        message.arg1 = arg1;
        message.obj = obj;
        handler.sendMessage(message);
    }

}
