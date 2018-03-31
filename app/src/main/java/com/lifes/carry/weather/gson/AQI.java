package com.lifes.carry.weather.gson;

/**
 * @author Administrator
 * @time 2018-03-02 15:49
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class AQI {

    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;
    }

}
