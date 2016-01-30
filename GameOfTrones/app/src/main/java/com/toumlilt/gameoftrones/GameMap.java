package com.toumlilt.gameoftrones;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by waye on 30/01/2016.
 */
public class GameMap implements OnMapReadyCallback, LocationListener {

    private GoogleMap googleMap;
    LocationManager locationManager;

    public GameMap(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        this.googleMap = googleMap;

        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.getUiSettings().setCompassEnabled(true);
        this.googleMap.setOnMyLocationChangeListener(myLocationChangeListener);
        this.googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                System.out.println("--------->>>><<<<<--------");
                Location location = googleMap.getMyLocation();
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                if(googleMap != null){
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                }
                return true;
            }
        });

        //Location tmp = this.googleMap.getMyLocation();
        //System.out.println("----->" + tmp.getLatitude() +","+tmp.getLongitude());
        //this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.8534100, 2.3488000), 15));
        googleMap.addMarker(new MarkerOptions().
                position(googleMap.getCameraPosition().target).title("TutorialsPoint"));
    }

    public void addSanitary(Sanitary sanitary) {
        googleMap.addMarker(new MarkerOptions().
                position(new LatLng(sanitary.getLatitude(), sanitary.getLongitude()))
                .title(sanitary.getRemainingLife() + "pdv"));
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("HELLLLLLLLO");
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

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            System.out.println("--------->>>><<<<<--------");
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            if(googleMap != null){
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        }
    };

}
