package com.lifes.carry.weather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author Administrator
 * @time 2018-02-26 0:40
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);


    }
}
