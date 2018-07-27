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
import android.widget.TextView;

import com.javarunner.weather.datamodel.LocationData;

import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final int LOCATION_UPDATES_TIME = 10000;
    public static final int LOCATION_UPDATES_DISTANCE = 1000;
    public static final int PERMISSION_REQUEST_CODE = 10;
    public static final String API_KEY = "8SHxlmx5AXHFthoHiturDohOThhcbFBl";
    public static final String BASE_URL = "http://dataservice.accuweather.com/";

    private LocationManager locationManager;
    private AccuWeather accuWeather;

    private TextView tvLocationName;
    private TextView tvLocationRegion;
    private TextView tvLatitude;
    private TextView tvLongitude;
    private TextView tvAccuracy;
    private TextView tvStatus;

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
        tvLatitude = findViewById(R.id.tv_latitude);
        tvLongitude = findViewById(R.id.tv_longitude);
        tvAccuracy = findViewById(R.id.tv_accuracy);
        tvStatus = findViewById(R.id.tv_status);
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
                PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissions();
            return;
        }

        tvStatus.setText(R.string.request_location_updates);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        locationManager.requestLocationUpdates(locationManager.getBestProvider(criteria, true),
                LOCATION_UPDATES_TIME,
                LOCATION_UPDATES_DISTANCE,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        tvStatus.setText(R.string.location_update_received);

                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        float accuracy = location.getAccuracy();

                        tvLatitude.setText(String.format(Locale.getDefault(), "%s: %.6f",
                                getString(R.string.latitude),
                                latitude));

                        tvLongitude.setText(String.format(Locale.getDefault(), "%s: %.6f",
                                getString(R.string.longitude),
                                longitude));

                        tvAccuracy.setText(String.format(Locale.getDefault(), "%s: %.2f",
                                getString(R.string.accuracy),
                                accuracy));

                        requestLocation(latitude, longitude);
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

    private void requestLocation(double latitude, double longitude) {
        accuWeather.getLocationData(API_KEY, String.format(Locale.ROOT, "%.3f,%.3f", latitude, longitude), getString(R.string.language_code))
        .enqueue(new Callback<LocationData>() {
            @Override
            public void onResponse(Call<LocationData> call, Response<LocationData> response) {
                LocationData locationData;

                if (response.body() == null) return;
                if ((locationData = response.body()) == null) return;

                tvLocationName.setText(locationData.getLocalizedName());
                tvLocationRegion.setText(String.format(Locale.getDefault(), "%s, %s",
                        locationData.getAdministrativeArea().getLocalizedName(),
                        locationData.getCountry().getLocalizedName()));
            }

            @Override
            public void onFailure(Call<LocationData> call, Throwable t) {
                tvStatus.setText("Get location data: failure");
            }
        });
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationCoordinates();
            }
        }
    }
}
