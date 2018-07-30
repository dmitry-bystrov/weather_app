package com.javarunner.weather;

import com.javarunner.weather.datamodel.LocationData;
import com.javarunner.weather.datamodel.WeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccuWeather {
    @GET("locations/v1/cities/geoposition/search")
    Call<LocationData> getLocationData(@Query("apikey") String apikey,
                                       @Query("q") String coordinates,
                                       @Query("language") String language);

    @GET("currentconditions/v1/{location}")
    Call<WeatherData[]> getWeatherData(@Path("location") String location,
                                       @Query("apikey") String apikey,
                                       @Query("language") String language,
                                       @Query("details") String details);
}
