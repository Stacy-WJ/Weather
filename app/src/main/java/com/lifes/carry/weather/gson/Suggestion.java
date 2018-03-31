package com.lifes.carry.weather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * @author Administrator
 * @time 2018-03-02 16:34
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class Suggestion  {
    @SerializedName("comf")
    public Comfort comfort;
    @SerializedName("cw")
    public CarWash carwash;
    public Sport sport;
     public class Comfort {
         @SerializedName("txt")
         public String info;

     }
    public class CarWash{
        @SerializedName("txt")
        public String info;
    }
    public class Sport{
        @SerializedName("txt")
        public String info;
    }
}
