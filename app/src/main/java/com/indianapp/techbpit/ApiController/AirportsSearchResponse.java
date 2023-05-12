package com.indianapp.techbpit.ApiController;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AirportsSearchResponse {
    @SerializedName("results")
    @Expose
    public List<AirportResult> results;
}
