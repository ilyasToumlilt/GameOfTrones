package com.toumlilt.gameoftrones;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Activité principale de l'application,
 * elle gère d'un côté l'affichage de la map et les événements qui y sont liés, et d'un autre
 * côté le moteur du jeu et la relation entre le modèle, le chargement des données et leur
 * affichage sur la map.
 */
public class GameActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    /**
     * Le joueur actuel.
     */
    private Player player;

    /**
     * Les sanisettes
     */
    private ArrayList<Sanitary> sanitaryList;
    /**
     * Les armes
     */
    private ArrayList<Weapon> weaponList;
    private GotDbHelper sh;
    private DownloadSanitary ds;
    private Sanitary currentSanitary;
    private Marker currentMarker;

    public final static int PROFILE_REQUEST = 1;

    public final static String EXTRA_USERNAME = "com.toumlilt.gameottrones.USERNAME";
    public final static String EXTRA_USERDESC = "com.toumlilt.gameottrones.USERDESC";

    /* Map attributes */
    private MapFragment mMapFragment;
    private GoogleMap googleMap;
    private Location mCurrentLocation = null;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private CircleOptions mCircleOptions;
    private Circle mCurrentCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* gestion du floating action button, qui représentera une attaque */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new GameEngine());

        /* DrawerLayout setup */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /* Getting intent from SignUpActivity, with player info */
        Intent intent = getIntent();
        String message = intent.getStringExtra(SignUpActivity.EXTRA_MESSAGE);
        String desc_msg = intent.getStringExtra(this.EXTRA_USERDESC);

        /* creating player */
        this.player = new Player(message, desc_msg);

        /* nav_view setup */
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /* setting up player informations in the navigationView's headerView */
        View header = navigationView.getHeaderView(0);
        TextView usernameNavTV = (TextView) header.findViewById(R.id.usernameNavTextView);
        usernameNavTV.setText(this.player.getUsername());
        TextView userdescNavTV = (TextView) header.findViewById(R.id.userdescNavTextView);
        userdescNavTV.setText(this.player.getUserdesc());

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if (status != ConnectionResult.SUCCESS) {
            // Google Play Services are not available
            System.out.println("ERROR: CONNECTION TO GOOGLE PLAY SERVICES FAILED");

        } else {
            // Google Play Services are available
            // so we can initialize our map services and view fragment
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            // map view fragment
            mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            // i'll need a map reference
            this.googleMap = mMapFragment.getMap();
            // setting listner
            mMapFragment.getMapAsync(this);

            // marker's event responding
            this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // lors d'un click sur un marker on fait une simple mise à jour
                    currentMarker = marker;
                    currentSanitary = getSanitaryFromLatLng(marker.getPosition());
                    return false;
                }
            });
        }

        // Model constructing
        this.ds = new DownloadSanitary();
        this.sh = new GotDbHelper(this);

        // some cool initial setup
        this.initSanitaryList();
        this.drawSanitaryList();
        this.initWeaponList();
        this.getCurrentWeapon();
    }

    /**
     * Fait correspondre la latitude/longitude d'un marker à celle d'un Sanitary et le retourne.
     * */
    public Sanitary getSanitaryFromLatLng(LatLng l){
        for (Sanitary s:this.sanitaryList)
            if(s.getLatitude() == l.latitude && s.getLongitude() == l.longitude)
                return s;
        return null;
    }

    /**
     * Retourne l'arme courante de l'utilisateur.
     * Par défault, c'est la première de la liste.
     * La valeur est stockée dans une SharedPreference.
     * Elle est mise-à-jour par la WeaponActivity par l'utilisateur.
     * */
    public Weapon getCurrentWeapon() {
        int indCurWeapon;
        Context c = getApplicationContext();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        indCurWeapon = sp.getInt("ind_curr_weapon", 0);
        return this.weaponList.get(indCurWeapon);
    }

    /**
     * Valeurs en dur pour créer une liste d'arme.
     * */
    private void initWeaponList() {
        this.weaponList = new ArrayList<>();
        this.weaponList.add(new DrawableWeapon("Gun", 5, 10, R.drawable.gun));
        this.weaponList.add(new DrawableWeapon("Knife", 8, 5, R.drawable.knife));
        this.weaponList.add(new DrawableWeapon("AK47", 8, 15, R.drawable.ak47));
        this.weaponList.add(new DrawableWeapon("Sword", 8, 10, R.drawable.sword));
        this.weaponList.add(new DrawableWeapon("Saber", 8, 12, R.drawable.saber));
        this.weaponList.add(new DrawableWeapon("Trowel", 8, 20, R.drawable.trowel));
    }

    /**
     * Met les sanisettes de la BDD en mémoire.
     * Si la BDD ne contient aucune sanisette :
     *      - On récupère la liste à partir du JSON en ligne
     *      - On ajoute cette liste dans la BDD
     *      - On la garde la liste en mémoire
     * */
    private boolean initSanitaryList()
    {
        ArrayList<Sanitary> tmp;

        if(this.sanitaryList == null)
        {
            //BDD vide ?
            if(this.sh.count() == 0)
            {
                try {
                    //Récupère JSON en ligne
                    tmp = this.ds.execute().get();

                    if(tmp==null){
                        Snackbar.make(
                                findViewById(R.id.fab),
                                "Impossible de se connecter à http://opendata.paris.fr",
                                Snackbar.LENGTH_LONG
                        ).show();
                    }

                    //Ajout à la BDD
                    for (Sanitary s:tmp){
                        System.out.println("--->" + this.sh.insert(s));
                    }
                }
                catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    Snackbar.make(
                            findViewById(R.id.fab),
                            "Impossible to retrieve sanitaries",
                            Snackbar.LENGTH_LONG
                    ).show();
                    return false;
                }
            }
            //Garde la liste en mémoire
            this.sanitaryList = this.sh.getAll();
            System.out.println("--->" + this.sh.count());
        }
        return true;
    }

    /**
     * Ajoute un marqueur par sanisette.
     * */
    private void drawSanitaryList(){
        for(Sanitary s : this.sanitaryList)
            this.addSanitary(s, s.getRemainingLife() == 0);
    }


    /**
     * D'après la doc il est conseillé de faire le connect (resp. disconnect) au GoogleApiClient
     * dans le onStart (resp. onStop)
     */
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    /**
     * D'après la doc il est conseillé de faire le connect (resp. disconnect) au GoogleApiClient
     * dans le onStart (resp. onStop)
     */
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

    /**
     * Gestion des selections dans la navigationView
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) { // profileActivity
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivityForResult(intent, PROFILE_REQUEST);
        } else if (id == R.id.nav_map_view) { // GameActivity ( current )

        } else if (id == R.id.nav_weapons) { // WeaponsActivity
            Intent iw = new Intent(this, WeaponsActivity.class);
            iw.putExtra("weapons", this.weaponList);
            startActivity(iw);

        } else if (id == R.id.nav_share_realm) { // Not implemented

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Handling Activities that return's with a result.
     * ( it'll be the case here for our ProfileActivity )
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PROFILE_REQUEST) {
            // result from profileActivity
            if (resultCode == RESULT_OK) { // Make sure the request was successful
                // getting result data
                this.player.setUsername(data.getStringExtra(EXTRA_USERNAME));
                this.player.setUserdesc(data.getStringExtra(EXTRA_USERDESC));
                this.updateNavigationViewData();
            }
        }
    }

    /**
     * Mise à jour des données utilisateur dans la NavigationView
     * Les données sont récupérées depuis le Player Model
     */
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

    /**
     * options relatives à la LocationREquest
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Ajoute un marker à la carte.
     * La couleur du marqueur est différente si la sanisette est prise ou pas.
     * @param sanitary : le Sanitary représenté par le marker
     * @param isTaken : la vie du Sanitary est elle égale à zéro.
     * */
    public Marker addSanitary(Sanitary sanitary, Boolean isTaken)
    {
        BitmapDescriptor bdf = BitmapDescriptorFactory.defaultMarker(
                isTaken?BitmapDescriptorFactory.HUE_ORANGE:BitmapDescriptorFactory.HUE_CYAN
        );

        return googleMap.addMarker(new MarkerOptions().
                position(new LatLng(sanitary.getLatitude(), sanitary.getLongitude()))
                .title(sanitary.getRemainingLife() + "pdv")
                .icon(bdf)
        );
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // j'active la localisation
        this.googleMap.setMyLocationEnabled(true);

    }

    /**
     * Methode appelée à chaque changement de localisation
     * Je repositionne la caméra pour que la location soit toujours centrée
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
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

    /**
     * Méthode appelée quand la connection aux APIs est établie
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        // vérification des permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            System.out.println("--> PERMISSION OK");
        } else {
            System.out.println("--> PERMISSION KO");
            // demande de permission de localisation si ce n'est pas encore fait
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
         }

        // si aucune location n'existe on initialize les locations
        if(this.mCurrentLocation == null)
            this.locationSetup();
        // sinon on m'est à jour l'existant
        else
            this.updateSettings();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /**
     * Options relatives à l'initialisation de la localisation et des cartes
     */
    private void locationSetup() {
        // on active la localisation
        this.googleMap.setMyLocationEnabled(true);
        // on vire le bouton de repositionement
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        // au début on va initialiser la position courante à la
        // dernière position enregistrée ( getLastLocation() )
        this.createLocationRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if(mCurrentLocation != null){
            // une fois la localisation courante initializée

            // on centre la caméra dessus
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                    .zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            // puis on dessine le premier cercle d'attaque
            this.mCircleOptions = new CircleOptions()
                    .center(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                    .radius(10 * getCurrentWeapon().getScope())
                    .strokeColor(Color.RED)
                    .fillColor(Color.argb(150, 168, 210, 224));
            this.mCurrentCircle = googleMap.addCircle(this.mCircleOptions);
        }
    }

    /**
     * à chaque refactoring on redessine le cercle
     */
    private void updateSettings() {
        this.updateCircleSettings();
    }

    /**
     * Cette méthode est appelée lors d'une requête de demande de permissions
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // si la permission pour la localisation est acceptée
                    // on initialise nos données de localisation
                    this.locationSetup();
                } else {
                    // TODO notif
                }
                return;
            }

        }
    }

    /**
     * Méthode de mise à jour du cercle d'attaque
     */
    private void updateCircleSettings() {
        // on efface l'ancien cercle d'attaque
        this.mCurrentCircle.remove();
        // puis on redessine un nouveau, centré sur la postion courante;
        // avec un rayon de 10*la portée de l'arme courante.
        this.mCircleOptions = new CircleOptions()
                .center(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                .radius(this.getCurrentWeapon().getScope()*10)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(150, 168, 210, 224));
        this.mCurrentCircle = googleMap.addCircle(this.mCircleOptions);
    }

    public class GameEngine implements View.OnClickListener {

        // événement click sur le bouton
        @Override
        public void onClick(View view)
        {
            if(currentSanitary == null)
            {
                // si aucune sanisette n'est sélectionnée, on blame le joueur
                Snackbar.make(
                        view,
                        "You must select a Sanitary first",
                        Snackbar.LENGTH_LONG
                ).show();
            }
            else
            {
                // si une sanisette est sélectionnée, on doit l'attaquer :-)
                // on récupère la localisation de la sanisette sélectionnée
                Location sanLocation = new Location("sanitary");
                sanLocation.setLatitude(currentSanitary.getLatitude());
                sanLocation.setLongitude(currentSanitary.getLongitude());

                // on attaque la sanisette si elle est dans notre portée d'attaque :
                if(mCurrentLocation.distanceTo(sanLocation) <= (getCurrentWeapon().getScope() * 10))
                {
                    //et si elle est toujours vivante
                    if(currentSanitary.getRemainingLife()>0) {
                        // une attaque se défini par une mise à jour des points de vie restants
                        // de la sanisette courante
                        Integer newLife = currentSanitary.getRemainingLife() - getCurrentWeapon().getPv();
                        currentSanitary.setRemainingLife(
                                newLife < 0 ? 0 : newLife
                        );

                        // mise à jour du marker de la sanisette courante
                        // on le supprime puis on le réinsert
                        if (currentMarker != null) {
                            currentMarker.remove();
                        }
                        addSanitary(currentSanitary, currentSanitary.getRemainingLife() == 0);
                        sh.update(currentSanitary);

                        // on noritife l'utilisateur après l'attaque
                        Snackbar.make(
                                view,
                                "Sanitary hit ! Remaining life: " + currentSanitary.getRemainingLife(),
                                Snackbar.LENGTH_LONG
                        ).show();

                        // avec un petit son, pour le bonus :-)
                        MediaPlayer.create(
                                getApplicationContext(),
                                R.raw.hit_sound
                        ).start();
                    }
                    else{
                        Snackbar.make(
                                view,
                                "Already ours !",
                                Snackbar.LENGTH_LONG
                        ).show();
                    }
                }
                else{
                    // sinon si le marker de la sanisette ne se trouve pas dans notre portée d'attaque
                    // on rappelle à l'ordre le vilain utilisateur :-)
                    Snackbar.make(
                            view,
                            "Too far away !",
                            Snackbar.LENGTH_LONG
                    ).show();
                }
            }
        }
    }
}
