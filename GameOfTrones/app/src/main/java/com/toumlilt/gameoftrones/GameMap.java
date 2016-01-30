package com.toumlilt.gameoftrones;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by waye on 30/01/2016.
 */
public class GameMap implements OnMapReadyCallback {

    private GoogleMap googleMap;

    public GameMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().
                position(googleMap.getCameraPosition().target).title("TutorialsPoint"));
    }

    private void addSanitary(Sanitary sanitary) {
        googleMap.addMarker(new MarkerOptions().
                position(new LatLng(sanitary.getLatitude(), sanitary.getLongitude()))
                .title(sanitary.getRemainingLife() + "pdv"));
    }
}
