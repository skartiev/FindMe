package sk.com.findme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import sk.com.findme.Classes.FriendsObject;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.StrictMath.pow;


public class MapActivity extends ActionBarActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener, GoogleMap.OnCameraChangeListener {

    private GoogleMap googleMap;
    private GoogleMapOptions options;
    private Location lastLocation;
    private ArrayList<FriendsObject> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_map);

        googleMap = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .compassEnabled(false)
                .rotateGesturesEnabled(false)
                .tiltGesturesEnabled(false);
        googleMap.setMyLocationEnabled(true);

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        lastLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        LatLng position;
        Intent intent = getIntent();
        if(intent.hasExtra("array"))
        {
            friends = intent.getParcelableArrayListExtra("array");
            position = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            moveCameraToMyPosition(position);
        }
        else if(intent.hasExtra("friend"))
        {
            FriendsObject friend = intent.getParcelableExtra("friend");
            friends = new ArrayList<FriendsObject>();
            friends.add(friend);
            position = new LatLng(friend.getLat(),friend.getLongs());
            moveCameraToMyPosition(position);

        }
        else
        {
            position = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            moveCameraToMyPosition(position);
            friends = new ArrayList<FriendsObject>();
        }





        for(int i=0; i<friends.size(); i++)
        {
            FriendsObject object = friends.get(i);
            if(object.getLat()==0 && object.getLongs() == 0)
            {

            }
            else
            {
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(object.getLat(),object.getLongs()))
                        .title(object.getName()));
            }


        }

    }
    @Override
    protected void onPause() {
        super.onPause();

    }
    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdates();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {


    }

    @Override
    public void onConnected(Bundle bundle) {
        //lastLocation = LocationServices.FusedLocationApi.getLastLocation(clientApi);
        //if(lastLocation != null) {
          //  startLocationUpdates();
        //}
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    
    protected void startLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //LocationServices.FusedLocationApi.requestLocationUpdates(
          //      clientApi, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.lastLocation  = location;
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        Marker myPositioning = googleMap.addMarker(new MarkerOptions()
        .position(latLng).title("My Location"));
    }
    @Override
    public void onCameraChange(CameraPosition cameraPosition)
    {

    }

    double calculateDistance(double nLat1,double nLon1,double nLat2, double nLon2)
    {
        final int nRadius = 6371;
        double nDLat = (nLat2 - nLat1) * (3.14/180);
        double nDLon = (nLon2 - nLon1) * (3.14/180);
        double nA = Math.pow(sin(nDLat / 2), 2) + cos(nLat1) * cos(nLat2) * Math.pow(sin(nDLon / 2), 2);

        double nC = 2 * atan2( sqrt(nA), sqrt( 1 - nA ));
        double nD = nRadius * nC;
        return nD;
    }
    void moveCameraToMyPosition(LatLng position) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)      // Sets the center of the map to my position
                .zoom(15)                   // Sets the zoom
//              .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
}
