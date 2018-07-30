
package com.javarunner.weather.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherData {

    @SerializedName("LocalObservationDateTime")
    @Expose
    private String localObservationDateTime;
    @SerializedName("EpochTime")
    @Expose
    private int epochTime;
    @SerializedName("WeatherText")
    @Expose
    private String weatherText;
    @SerializedName("WeatherIcon")
    @Expose
    private int weatherIcon;
    @SerializedName("IsDayTime")
    @Expose
    private boolean isDayTime;
    @SerializedName("Temperature")
    @Expose
    private Temperature temperature;
    @SerializedName("RealFeelTemperature")
    @Expose
    private RealFeelTemperature realFeelTemperature;
    @SerializedName("RelativeHumidity")
    @Expose
    private int relativeHumidity;
    @SerializedName("DewPoint")
    @Expose
    private DewPoint dewPoint;
    @SerializedName("Wind")
    @Expose
    private Wind wind;
    @SerializedName("UVIndex")
    @Expose
    private int uVIndex;
    @SerializedName("UVIndexText")
    @Expose
    private String uVIndexText;
    @SerializedName("Visibility")
    @Expose
    private Visibility visibility;
    @SerializedName("ObstructionsToVisibility")
    @Expose
    private String obstructionsToVisibility;
    @SerializedName("CloudCover")
    @Expose
    private int cloudCover;
    @SerializedName("Pressure")
    @Expose
    private Pressure pressure;
    @SerializedName("MobileLink")
    @Expose
    private String mobileLink;
    @SerializedName("Link")
    @Expose
    private String link;

    public String getLocalObservationDateTime() {
        return localObservationDateTime;
    }

    public void setLocalObservationDateTime(String localObservationDateTime) {
        this.localObservationDateTime = localObservationDateTime;
    }

    public int getEpochTime() {
        return epochTime;
    }

    public void setEpochTime(int epochTime) {
        this.epochTime = epochTime;
    }

    public String getWeatherText() {
        return weatherText;
    }

    public void setWeatherText(String weatherText) {
        this.weatherText = weatherText;
    }

    public int getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(int weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public boolean isDayTime() {
        return isDayTime;
    }

    public void setDayTime(boolean dayTime) {
        isDayTime = dayTime;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public RealFeelTemperature getRealFeelTemperature() {
        return realFeelTemperature;
    }

    public void setRealFeelTemperature(RealFeelTemperature realFeelTemperature) {
        this.realFeelTemperature = realFeelTemperature;
    }

    public int getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(int relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    public DewPoint getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(DewPoint dewPoint) {
        this.dewPoint = dewPoint;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public int getuVIndex() {
        return uVIndex;
    }

    public void setuVIndex(int uVIndex) {
        this.uVIndex = uVIndex;
    }

    public String getuVIndexText() {
        return uVIndexText;
    }

    public void setuVIndexText(String uVIndexText) {
        this.uVIndexText = uVIndexText;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public String getObstructionsToVisibility() {
        return obstructionsToVisibility;
    }

    public void setObstructionsToVisibility(String obstructionsToVisibility) {
        this.obstructionsToVisibility = obstructionsToVisibility;
    }

    public int getCloudCover() {
        return cloudCover;
    }

    public void setCloudCover(int cloudCover) {
        this.cloudCover = cloudCover;
    }

    public Pressure getPressure() {
        return pressure;
    }

    public void setPressure(Pressure pressure) {
        this.pressure = pressure;
    }

    public String getMobileLink() {
        return mobileLink;
    }

    public void setMobileLink(String mobileLink) {
        this.mobileLink = mobileLink;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
