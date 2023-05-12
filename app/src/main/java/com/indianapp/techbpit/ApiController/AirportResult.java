package com.indianapp.techbpit.ApiController;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AirportResult implements Serializable {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("code")
    @Expose
    public String code;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("cityCode")
    @Expose
    public String cityCode;

    @SerializedName("cityName")
    @Expose
    public String cityName;

    @SerializedName("countryName")
    @Expose
    public String countryName;

    @SerializedName("currencyCode")
    @Expose
    public String currencyCode;

    @SerializedName("isInAPopularCity")
    @Expose
    public boolean isInAPopularCity;
}
