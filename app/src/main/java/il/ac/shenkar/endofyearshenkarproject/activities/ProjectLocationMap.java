package il.ac.shenkar.endofyearshenkarproject.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import il.ac.shenkar.endofyearshenkarproject.R;
import il.ac.shenkar.endofyearshenkarproject.json.LocationJson;
import il.ac.shenkar.endofyearshenkarproject.utils.PermissionUtils;

/**
 * Google maps api and permissions for presenting projects on map
 */
public class ProjectLocationMap extends ShenkarActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /**
     * Request code for location permission request.
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final List<BitmapDescriptor> mImages = new ArrayList<BitmapDescriptor>();
    private TextView projectName;
    private Long projectId;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap projectMap;
    private Location mLastLocation;
    private GroundOverlay mGroundOverlay;
    private LocationJson college_location;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_location_map);
        projectId = getIntent().getLongExtra("id",0);



        projectName = (TextView)findViewById(R.id.textView_projectLocationMap_projectName);
        projectName.setText(getIntent().getStringExtra("name"));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.department_map);
        mapFragment.getMapAsync(this);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        projectMap = googleMap;

        enableMyLocation();
        college_location = StaticCollegeConfigJson.mMainConfig.getBuilding().getLocation();
        LatLng latLng = new LatLng(college_location.getLat(), college_location.getLng());
        projectMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, StaticCollegeConfigJson.mMainConfig.getBuilding().getZoom()));
        //set map as static

        mImages.clear();
        mImages.add(BitmapDescriptorFactory.fromResource(R.mipmap.overview));

        mGroundOverlay = projectMap.addGroundOverlay(new GroundOverlayOptions()
                .image(mImages.get(0))
                .position(latLng, 190f, 150f));
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
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Error");
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            System.out.println("mLastLocation.getLatitude()) ---------> "+ mLastLocation.getLatitude());
            System.out.println("mLastLocation.getLongitude()) ---------> "+ mLastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection problem", Toast.LENGTH_LONG).show();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (projectMap != null) {
            // Access to the location has been granted to the app.
            projectMap.setMyLocationEnabled(true);
        }
    }


    @Override
    void setObjectID() {

    }
}
