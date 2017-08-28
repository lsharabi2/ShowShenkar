package il.ac.shenkar.endofyearshenkarproject.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import il.ac.shenkar.endofyearshenkarproject.R;
import il.ac.shenkar.endofyearshenkarproject.adapters.PopupAdapter;
import il.ac.shenkar.endofyearshenkarproject.json.DepartmentJson;
import il.ac.shenkar.endofyearshenkarproject.json.DepartmentJsonStatic;
import il.ac.shenkar.endofyearshenkarproject.json.GsonRequest;
import il.ac.shenkar.endofyearshenkarproject.json.JsonURIs;
import il.ac.shenkar.endofyearshenkarproject.json.LocationJson;
import il.ac.shenkar.endofyearshenkarproject.json.ProjectJson;
import il.ac.shenkar.endofyearshenkarproject.json.ProjectJsonStatic;
import il.ac.shenkar.endofyearshenkarproject.utils.PermissionUtils;

/**
 * set requested map
 */
public class MapActivity extends ShenkarActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private GoogleMap mMap;
    private Long objectId;
    private String objectType;
    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

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

        objectId = getIntent().getLongExtra("objectId", 0);
        objectType = getIntent().getStringExtra("objectType");
    }

    @Override
    void setObjectID() {
    }

    /**
     * Check scanner/map results (project,department or general)
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();
        // if project
        if (objectType.equals("project")) {
            List<ProjectJson> projects = ProjectJsonStatic.getProjectJsonList();
            // if we already have the projects list from other activities no need to bring them again
            if (!projects.isEmpty()) {
                SetProjectMapFromList(objectId);
            } else {
                getProjectByID(objectId);
            }
            // if department
        } else if (objectType.equals("department")) {
            String strUrl = SetDepartmentMap(objectId);

            mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater(), strUrl));
            // if general
        } else if (objectType.equals("general")) {
            SetGeneralMap();
        }
    }

    /**
     * find objectId project in projects (when we don't have the list of projects from former activities)
     */
    private void getProjectByID(final Long projectId) {

        new AsyncTask<Void, Void, ProjectJson>() {
            @Override
            protected ProjectJson doInBackground(Void... params) {
                ProjectJson project = getProjectById(projectId);
                return project;
            }

            @Override
            protected void onPostExecute(ProjectJson project) {
                //show complition in UI
                //fill grid view with data
                if (project != null) {

                    SetProjectMap(project);
                }
            }
        }.execute();

    }

    /**
     * get project data by id from json
     */
    public ProjectJson getProjectById(long projectId) {
        try {
            final String url = JsonURIs.getProjectByIdUri(projectId);

            RequestFuture<ProjectJson> future = RequestFuture.newFuture();

            GsonRequest req = new GsonRequest(url, ProjectJson.class, null, future, future);

            RequestQueue tempQueue = Volley.newRequestQueue(getBaseContext());

            // Add the request to the RequestQueue.
            tempQueue.add(req);

            ProjectJson response = future.get(10, TimeUnit.SECONDS);
            return response;
        } catch (InterruptedException e) {
            //    Log.d(TAG, "interrupted error");
            e.printStackTrace();
        } catch (ExecutionException e) {
            //    Log.d(TAG, "execution error");
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Find department by id and set her map
     */
    private String SetDepartmentMap(final Long departmentId) {
        List<DepartmentJson> deparment = DepartmentJsonStatic.getDepartmentJsonList();
        String strUrl = "";
        for (DepartmentJson departmentJson : deparment) {
            if (departmentJson.getId() == departmentId) {
                SetMapByDepartmentName(departmentJson);
                strUrl = departmentJson.getImageUrl();
                return strUrl;
            }
        }
        return strUrl;
    }

    /**
     * Set project's department map and their markers
     */
    private void SetProjectMap(ProjectJson project) {

        SetDepartmentMap(project.getDepartmentId());
        AddMarkerByLocationContent(project);
    }

    /**
     * This function handle the case we do have the list of projects and we want to show the requested project on map
     */
    private void SetProjectMapFromList(final Long projectId) {
        List<ProjectJson> projectList = ProjectJsonStatic.getProjectJsonList();
        for (ProjectJson project : projectList) {
            if (project.getId() == projectId) {
                SetDepartmentMap(project.getDepartmentId());
                AddMarkerByLocationContent(project);
            }

        }
    }

    /**
     * Add project marker
     */
    private void AddMarkerByLocationContent(ProjectJson project) {
        LocationJson locationJson = project.getLocation();
        double lat = locationJson.getLat();
        double lng = locationJson.getLng();
        LatLng location = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(location).title(project.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

    }
    /*
     * Set General Map
     * */
    public void SetGeneralMap(){
        SetMapByDepartmentName(null);
        mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater(),""));
    }

    /**
     * Set department or general marker
     */
    void AddMarker(LatLng location,String text){
        mMap.addMarker(new MarkerOptions().position(location).title(text));
    }

    /**
     * Set department map and marker
     */
    private void SetMapByDepartmentName(DepartmentJson departmentJson) {
        LatLng location;
        String department = "";
        String path = "";
        String building = "";
        LatLng locationBuild;
        long zoom;
        if (departmentJson == null) { // general shenkar
            LocationJson locationJson = StaticCollegeConfigJson.mMainConfig.getBuilding().getLocation();
            path = StaticCollegeConfigJson.mMainConfig.getPath();
            double lat = locationJson.getLat();
            double lng = locationJson.getLng();
            location = new LatLng(lat, lng);
            locationBuild = location;

            zoom = StaticCollegeConfigJson.mMainConfig.getBuilding().getZoom();
            // SHENKAR
        } else {
            path = departmentJson.getPath();
            building = departmentJson.getBuilding().getLocationDescription() + departmentJson.getName();
            LocationJson buldingLocation = departmentJson.getBuilding().getLocation();

            double latBuild = buldingLocation.getLat();
            double lngBild = buldingLocation.getLng();
            locationBuild = new LatLng(latBuild, lngBild);

            double lat = departmentJson.getLocation().getLat();
            double lng = departmentJson.getLocation().getLng();
            location = new LatLng(lat, lng);
            zoom = departmentJson.getBuilding().getZoom();
        }

        // Zoom change according to user demand
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationBuild, zoom));
        AddMarker(location, building);

        setUpMap(path);
    }

    /**
     * here we address the server which whom we download the maps
     */
    private void setUpMap(final String path) {


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public synchronized URL getTileUrl(int x, int y, int zoom) {
                int reversedY = (1 << zoom) - y - 2;
                // Here you can change the url to your institute's server which holds the maps
                String s = String.format(Locale.US, "http://megastar.co.il/EPSG3857/" + path + "/%d/%d/%d.png", zoom, x, y);
                //  You can try another server which we upload the maps into if server fail: "http://shenkar.xyz/maps/1/"
                URL url = null;
                try {
                    url = new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
                return url;
            }
        };

        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

}

