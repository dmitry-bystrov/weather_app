
package com.javarunner.weather.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("Direction")
    @Expose
    private Direction direction;
    @SerializedName("Speed")
    @Expose
    private Speed speed;

}
