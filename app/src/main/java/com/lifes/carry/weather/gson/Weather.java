package com.lifes.carry.weather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Administrator
 * @time 2018-03-02 18:27
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}
