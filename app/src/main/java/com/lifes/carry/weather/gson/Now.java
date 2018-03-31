package com.lifes.carry.weather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * @author Administrator
 * @time 2018-03-02 16:31
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;
    public class More{
        @SerializedName("txt")
        public String info;
    }
}
