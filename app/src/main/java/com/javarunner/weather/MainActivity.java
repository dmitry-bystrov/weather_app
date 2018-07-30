package com.javarunner.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.javarunner.weather.datamodel.LocationData;
import com.javarunner.weather.datamodel.WeatherData;
import com.javarunner.weather.datamodel.Wind;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final int LOCATION_UPDATES_TIME = 900000;
    public static final int LOCATION_UPDATES_DISTANCE = 5000;
    public static final int PERMISSION_REQUEST_CODE = 10;
    public static final String API_KEY = "XyxId075ap3OrAO5bigNVaGZMXUB89rG";
    public static final String BASE_URL = "http://dataservice.accuweather.com/";
    public static final String TRUE = "true";

    private LocationManager locationManager;
    private AccuWeather accuWeather;

    private TextView tvLocationName;
    private TextView tvLocationRegion;
    private TextView tvWeatherText;
    private TextView tvWindText;
    private TextView tvTemperature;
    private TextView tvHumidity;
    private ImageView ivWeatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initRetrofit();
        requestLocationCoordinates();
    }

    private void initView() {
        tvLocationName = findViewById(R.id.tv_location_name);
        tvLocationRegion = findViewById(R.id.tv_location_region);
        tvWeatherText = findViewById(R.id.tv_weather_text);
        tvWindText = findViewById(R.id.tv_wind_text);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvHumidity = findViewById(R.id.tv_humidity);
        ivWeatherIcon = findViewById(R.id.iv_weather_icon);
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build();

        accuWeather = retrofit.create(AccuWeather.class);
    }

    private OkHttpClient createOkHttpClient() {
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        return httpClient.build();
    }

    private void requestLocationCoordinates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissions();
            return;
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String bestProvider = locationManager.getBestProvider(criteria, true);
        updateLocation(locationManager.getLastKnownLocation(bestProvider));

        locationManager.requestLocationUpdates(bestProvider,
                LOCATION_UPDATES_TIME,
                LOCATION_UPDATES_DISTANCE,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        updateLocation(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
    }

    private void requestLocationData(double latitude, double longitude) {
        accuWeather.getLocationData(API_KEY, String.format(Locale.ROOT, "%.3f,%.3f", latitude, longitude), getString(R.string.language_code))
                .enqueue(new Callback<LocationData>() {
                    @Override
                    public void onResponse(Call<LocationData> call, Response<LocationData> response) {
                        LocationData locationData;

                        if ((locationData = response.body()) == null) return;

                        tvLocationName.setText(locationData.getLocalizedName());
                        tvLocationRegion.setText(String.format(Locale.getDefault(), "%s, %s",
                                locationData.getAdministrativeArea().getLocalizedName(),
                                locationData.getCountry().getLocalizedName()));

                        requestWeatherData(locationData);
                    }

                    @Override
                    public void onFailure(Call<LocationData> call, Throwable t) {
                    }
                });
    }

    private void requestWeatherData(LocationData locationData) {
        accuWeather.getWeatherData(locationData.getKey(), API_KEY, getString(R.string.language_code), TRUE)
                .enqueue(new Callback<WeatherData[]>() {
                    @Override
                    public void onResponse(Call<WeatherData[]> call, Response<WeatherData[]> response) {
                        WeatherData[] weatherData;

                        if ((weatherData = response.body()) == null) return;
                        if (weatherData.length < 1) return;

                        tvWeatherText.setText(weatherData[0].getWeatherText());

                        Wind wind = weatherData[0].getWind();
                        if (wind != null) {
                            tvWindText.setText(String.format(Locale.getDefault(), "%s: %.0f %s",
                                    getResources().getString(R.string.wind),
                                    wind.getSpeed().getMetric().getValue(),
                                    wind.getSpeed().getMetric().getUnit()));
                        }

                        float temperatureValue = weatherData[0].getTemperature().getMetric().getValue();
                        tvTemperature.setText(String.format(Locale.getDefault(), "%s: %s%.0f%s",
                                getResources().getString(R.string.temperature),
                                temperatureValue > 0 ? "+" : "",
                                temperatureValue,
                                "°C"));

                        int humidity = weatherData[0].getRelativeHumidity();
                        tvHumidity.setText(String.format(Locale.getDefault(), "%s: %d%%",
                                getResources().getString(R.string.humidity),
                                humidity));

                        updateWeatherIcon(weatherData[0].getWeatherIcon());
                    }

                    @Override
                    public void onFailure(Call<WeatherData[]> call, Throwable t) {

                    }
                });
    }

    private void updateWeatherIcon(int weatherIconId) {
        if (weatherIconId > 0) {
            Picasso
                    .with(this)
                    .load(String.format(Locale.ROOT, "%s%02d%s", "https://developer.accuweather.com/sites/default/files/", weatherIconId, "-s.png"))
                    .into(ivWeatherIcon);
        }
    }

    private void updateLocation(Location location) {
        if (location == null) return;
        requestLocationData(location.getLatitude(), location.getLongitude());
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                requestLocationCoordinates();
            }
        }
    }
}
