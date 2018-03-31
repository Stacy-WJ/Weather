package com.lifes.carry.weather.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.lifes.carry.weather.db.City;
import com.lifes.carry.weather.db.County;
import com.lifes.carry.weather.db.Province;
import com.lifes.carry.weather.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Administrator
 * @time 2018-02-26 17:52
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class Utility {
    /*
    解析和处理省级服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setprovinceCode(provinceObject.getInt("id"));
                    province.save();
                }return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /*
    解析和处理市级服务器返回的省级数据
     */
    public  static boolean handleCityResponse(String response , int proviceId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCityies = new JSONArray(response);
                for (int i = 0; i < allCityies.length(); i++) {
                    JSONObject cityObject = allCityies.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(proviceId);
                    city.save();
                }return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }return false;
    }
    /*
    解析和处理县级服务器返回的省级数据
     */
    public static boolean handleCountyResponse(String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.optString("name"));
//                    opt如果没有key对应的值 则返回默认值 而不是空指针
                    county.setId(countyObject.getInt("id"));
                    county.setCityId(cityId);
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.save();
                }return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static Weather handleWeatherResponse (String response){
        try {
//            JSONObject jsonObject = new JSONObject(response);
//            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
//            String weatherContent = jsonArray.getJSONObject(0).toString();
//            return new Gson().fromJson(weatherContent,Weather.class);
//
        JSONObject jsonObject =  new JSONObject(response) ;
         JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return  new Gson().fromJson(weatherContent,Weather.class);

        } catch (JSONException e) {
            e.printStackTrace();

        }
        return null;
    }
}