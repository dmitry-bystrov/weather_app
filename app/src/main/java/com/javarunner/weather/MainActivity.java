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

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final int LOCATION_UPDATES_TIME = 10000;
    public static final int LOCATION_UPDATES_DISTANCE = 1000;
    public static final int PERMISSION_REQUEST_CODE = 10;
    private LocationManager locationManager;

    private TextView tvLatitude;
    private TextView tvLongitude;
    private TextView tvAccuracy;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        requestLocation();
    }

    private void initView() {
        tvLatitude = findViewById(R.id.tv_latitude);
        tvLongitude = findViewById(R.id.tv_longitude);
        tvAccuracy = findViewById(R.id.tv_accuracy);
        tvStatus = findViewById(R.id.tv_status);
    }

    private void requestLocation() {
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

                        tvLatitude.setText(String.format(Locale.getDefault(), "%s: %.2f",
                                getString(R.string.latitude),
                                latitude));

                        tvLongitude.setText(String.format(Locale.getDefault(), "%s: %.2f",
                                getString(R.string.longitude),
                                longitude));

                        tvAccuracy.setText(String.format(Locale.getDefault(), "%s: %.2f",
                                getString(R.string.accuracy),
                                accuracy));
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
                requestLocation();
            }
        }
    }
}
