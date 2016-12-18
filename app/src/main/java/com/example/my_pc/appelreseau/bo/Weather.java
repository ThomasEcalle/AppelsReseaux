package com.example.my_pc.appelreseau.bo;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by My-PC on 17/12/2016.
 */

public class Weather
{
    private String city;
    private String date;
    private int temperatureMin;
    private int temperatureMax;

    public Weather (JSONObject jsonObject)
    {
        this.city = jsonObject.optString("city");
        this.date = jsonObject.optString("date");
        this.temperatureMin = jsonObject.optInt("temperatureMin");
        this.temperatureMax = jsonObject.optInt("temperatureMax");

        Log.d("thomasecalle","New Weather " +
                ":\n city = " + city +
                "\n date = " + date +
                "\n temp min = " + temperatureMin +
                "\n temp max  = "+ temperatureMax);
    }

    public int getTemperatureMin()
    {
        return temperatureMin;
    }

    public int getTemperatureMax()
    {
        return temperatureMax;
    }

    public String getCity()
    {
        return city;
    }

    public String getDate()
    {
        return date;
    }
}
