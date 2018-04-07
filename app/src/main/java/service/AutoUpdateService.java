package service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.lifes.carry.weather.gson.Weather;
import com.lifes.carry.weather.util.HttpUtil;
import com.lifes.carry.weather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.lifes.carry.weather.util.Utility.handleWeatherResponse;

public class AutoUpdateService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager= (AlarmManager)getSystemService(ALARM_SERVICE);
        int theHour = 8*60*60*1000;//8小时的毫秒数值
        long triggerAtTime = SystemClock.elapsedRealtime()+theHour;
        Intent i = new Intent(this,AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateBingPic() {
        String picUrl =  "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(picUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString(bingPic,"bingPic");
                editor.apply();

            }
        });
    }


    //                    因为只是服务 所以只要更改 sharedpreferences数据就可以
    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather",null);
        if (weatherString!=null){
            //            有缓存时直接解析数据
            Weather weather = handleWeatherResponse(weatherString);
            final String weatherId = weather.basic.weatherId;
            String weatherUrl = "http://guolin.tech/api/weather?cityid="
                    +weatherId+"&key=39ac6efd82604120b22b41abd904f30f";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String responseText = response.body().string();
                    Weather weather = Utility.handleWeatherResponse(responseText);
                    if (weather!=null && "ok".equals(weather.status)){
                        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather",responseText);
                        editor.apply();
                    }
                }
            });
        }
    }
}
