package com.example.daidaijie.syllabusapplication.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.daidaijie.syllabusapplication.App;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;

/**
 * Created by daidaijie on 2016/10/29.
 */

public class ShareWXUtil {

    public static void shareUrl(String url, String title, String desc, Bitmap bitmap, int scene) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = desc;
        msg.setThumbImage(bitmap);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = scene;
        App.getApi().sendReq(req);
    }

}
