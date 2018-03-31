package com.lifes.carry.weather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * @author Administrator
 * @time 2018-03-02 14:07
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;
    public class  Update{

        @SerializedName("loc")
        public String updateTime;


    }

}
