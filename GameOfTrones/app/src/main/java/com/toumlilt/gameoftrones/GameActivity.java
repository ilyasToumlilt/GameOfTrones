package com.toumlilt.gameoftrones;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GameActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Player player;

    public final static int PROFILE_REQUEST = 1;

    public final static String EXTRA_USERNAME = "com.toumlilt.gameottrones.USERNAME";
    public final static String EXTRA_USERDESC = "com.toumlilt.gameottrones.USERDESC";

    //private GameMap gameMap;
    private MapFragment mMapFragment;
    private GoogleMap googleMap;
    private Location mCurrentLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /*** Getting intent from SignUpActivity ***/
        Intent intent = getIntent();
        String message = intent.getStringExtra(SignUpActivity.EXTRA_MESSAGE);

        /* creating player */
        this.player = new Player(message, "");

        /* nav_view */
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        TextView usernameNavTV = (TextView) header.findViewById(R.id.usernameNavTextView);
        usernameNavTV.setText(this.player.getUsername());

        TextView userdescNavTV = (TextView) header.findViewById(R.id.userdescNavTextView);
        userdescNavTV.setText(this.player.getUserdesc());

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            System.out.println("CONNECTION FAILED");

        } else { // Google Play Services are available
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            this.googleMap = mMapFragment.getMap();
            mMapFragment.getMapAsync(this);

        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivityForResult(intent, PROFILE_REQUEST);
        } else if (id == R.id.nav_map_view) {

        } else if (id == R.id.nav_weapons) {

        } else if (id == R.id.nav_share_realm) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PROFILE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                this.player.setUsername(data.getStringExtra(EXTRA_USERNAME));
                this.player.setUserdesc(data.getStringExtra(EXTRA_USERDESC));
                this.updateNavigationViewData();
            }
        }
    }

    private void updateNavigationViewData() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);

        TextView usernameNavTV = (TextView) header.findViewById(R.id.usernameNavTextView);
        usernameNavTV.setText(this.player.getUsername());

        TextView userdescNavTV = (TextView) header.findViewById(R.id.userdescNavTextView);
        userdescNavTV.setText(this.player.getUserdesc());
    }

    /***********************************************************************************************
     * Map
     **********************************************************************************************/
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.addMarker(new MarkerOptions().
                position(googleMap.getCameraPosition().target).title("TutorialsPoint"));

    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("------> Location changed to " + location.toString());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(15).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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

    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            System.out.println("--> OK");
        } else {
            System.out.println("--> KO");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
         }
        this.locationSetup();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void locationSetup() {
        this.googleMap.setMyLocationEnabled(true);
        this.createLocationRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if(mCurrentLocation != null){
            System.out.println("------>" + mCurrentLocation.toString());
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                    .zoom(15).build();

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            googleMap.addCircle(new CircleOptions()
                    .center(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                    .radius(100)
                    .strokeColor(Color.RED)
                    .fillColor(Color.BLUE));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    this.locationSetup();
                } else {
                    // TODO notif
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
