package com.toumlilt.gameoftrones;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

public class GameActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private Player player;

    public final static int PROFILE_REQUEST = 1;

    public final static String EXTRA_USERNAME = "com.toumlilt.gameottrones.USERNAME";
    public final static String EXTRA_USERDESC = "com.toumlilt.gameottrones.USERDESC";

    private GoogleMap googleMap;


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

        /* setting up map's fragment callback */
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //Marker TP = googleMap.addMarker(new MarkerOptions().
        //        position(googleMap.getCameraPosition().target).title("TutorialsPoint"));
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
     * Map control
     **********************************************************************************************/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().
                position(googleMap.getCameraPosition().target).title("TutorialsPoint"));
    }

    private void addSanisette(Sanisette sanitary) {
        googleMap.addMarker(new MarkerOptions().
                position(new LatLng(sanitary.getLatitude(), sanitary.getLongitude()))
                .title("TutorialsPoint"));
    }
}