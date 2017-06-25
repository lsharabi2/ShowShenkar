package il.ac.shenkar.endofyearshenkar.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import il.ac.shenkar.endofyearshenkar.R;
import il.ac.shenkar.endofyearshenkar.adapters.PopupAdapter;
import il.ac.shenkar.endofyearshenkar.json.DepartmentJson;
import il.ac.shenkar.endofyearshenkar.json.DepartmentJsonStatic;
import il.ac.shenkar.endofyearshenkar.json.LocationJson;
import il.ac.shenkar.endofyearshenkar.json.ProjectJson;
import il.ac.shenkar.endofyearshenkar.json.ProjectJsonStatic;
import il.ac.shenkar.endofyearshenkar.utils.PermissionUtils;

public class MapActivity extends ShenkarActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final LatLng PERNIK = new LatLng(32.0900466 , 34.8035959);
    private static final LatLng MITSHLE = new LatLng(32.089928, 34.802239);
    private static final LatLng INTERIOR_DESIGN = new LatLng(32.09030615669672, 34.803183311601877);
    private static final LatLng SHENKAR = new LatLng(32.090023, 34.803151);
    private static final LatLng ELIT = new LatLng(32.08264557800064, 34.80440218001604);
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
        System.out.println("Liron objectId =" + objectId);
        System.out.println("Liron objectType =" + objectType);
    }

    @Override
    void setObjectID() {
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();
        if (objectType.equals("project")) {
            SetProjectMap(objectId);
        } else if (objectType.equals("department")) {
            String strUrl = SetDepartmentMap(objectId);

            mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater(), strUrl));
        } else if (objectType.equals("general")) {
            SetGeneralMap();
        }
    }

    private String SetDepartmentMap(final Long departmentId) {
        List<DepartmentJson> deparment = DepartmentJsonStatic.getDepartmentJsonList();
        String strUrl = "";
        for (DepartmentJson departmentJson : deparment) {
            if (departmentJson.getId() == departmentId) {
                LocationJson locationJson = departmentJson.getLocation();
                double lat = locationJson.getLat();
                double lng = locationJson.getLng();
                LatLng location = new LatLng(lat, lng);
                SetMapByDepartmentName(departmentJson.getName(), location);
                strUrl = departmentJson.getImageUrl();
                return strUrl;
                //   mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater(),departmentJson.getImageUrl()));
            }
        }
        return strUrl;
    }

    private void SetProjectMap(final Long projectId) {
        List<ProjectJson> projectList = ProjectJsonStatic.getProjectJsonList();
        for (ProjectJson project : projectList) {
            if (project.getId() == projectId) {
                SetDepartmentMap(project.getDepartmentId());
                AddMarkerByLocationContent(project);
            }

        }
    }

    private void AddMarkerByLocationContent(ProjectJson project) {
        LocationJson locationJson = project.getLocation();
        double lat = locationJson.getLat();
        double lng = locationJson.getLng();
        LatLng location = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(location).title(project.getName()));


        //TODO check if no location in the project

    }
    /*
     *   Set General Map
     * */
    public void SetGeneralMap(){
        SetMapByDepartmentName("שנקר", SHENKAR);
        mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater(),""));
    }

    /*
     *   Set Department Map
     * */
//    public void SetDepartmentMap(final Long departmentId){
//        final DepartmentApi departmentApi = new DepartmentApi.Builder(
//                AndroidHttp.newCompatibleTransport(),
//                new JacksonFactory(),
//                new HttpRequestInitializer() {
//                    @Override
//                    public void initialize(HttpRequest request) throws IOException {
//
//                    }
//                }).setRootUrl(Constants.ROOT_URL).build();
//
//        new AsyncTask<Void, Void, Department>() {
//            @Override
//            protected Department doInBackground(Void... params) {
//                try {
//                    return departmentApi.getDepartment(departmentId).execute();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Department department) {
//                if (department != null) {
//                    SetMapByDepartmentName(department.getName());
//                    mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater(),department.getImageUrl()));
//                }
//            }
//        }.execute();
//    }
//
//    /*
//     *   Set Project Map
//     * */
//    public void SetProjectMap(final Long projectId) {
//        final ProjectApi projectApi = new ProjectApi.Builder(
//                AndroidHttp.newCompatibleTransport(),
//                new JacksonFactory(),
//                new HttpRequestInitializer() {
//                    @Override
//                    public void initialize(HttpRequest request) throws IOException {
//
//                    }
//                }).setRootUrl(Constants.ROOT_URL).build();
//
//
//        new AsyncTask<Void, Void, Project>() {
//            @Override
//            protected Project doInBackground(Void... params) {
//                try {
//                    return projectApi.getProject(projectId).execute();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Project project) {
//                if (project != null) {
//                    SetMapByDepartmentName(project.getDepartment());
//                    AddMarkerByLocationContent(Long.parseLong(project.getContentId()), project.getName());
//                }
//            }
//        }.execute();
//    }
//
//    private void AddMarkerByLocationContent(final Long contentId, final String text){
//        final ContentApi contentApi = new ContentApi.Builder(
//                AndroidHttp.newCompatibleTransport(),
//                new JacksonFactory(),
//                new HttpRequestInitializer() {
//                    @Override
//                    public void initialize(HttpRequest request) throws IOException {
//
//                    }
//                }).setRootUrl(Constants.ROOT_URL).build();
//
//        new AsyncTask<Void, Void,Content>() {
//            @Override
//            protected Content doInBackground(Void... params) {
//                Content content = null;
//                try {
//                    content = contentApi.getContent(contentId).execute();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return content;
//            }
//
//            @Override
//            protected void onPostExecute(Content content) {
//                if (content != null) {
//                    // Add a marker of project by content-location
//                    LatLng location = null;
//                   try {
//                       location = new LatLng(content.getLocation().getLat(), content.getLocation().getLng());
//                       mMap.addMarker(new MarkerOptions().position(location).title(text));
//                   }catch (Exception exc) {
//                       mMap.addMarker(new MarkerOptions().position(SHENKAR).title(text));
//                   }
//                }
//            }
//        }.execute();
//    }

    void AddMarker(LatLng location,String text){
        mMap.addMarker(new MarkerOptions().position(location).title(text));
    }

    private void SetMapByDepartmentName(String department, LatLng location) {
        String path = null;
        String building = null;
        switch (department) {
            case "עיצוב תכשיטים": {
                path = "Mitchle/3";
                building = "Mitchle";
                break;
            }
            case "תואר שני בעיצוב": {
                path = "Mitchle/7";
                building = "Mitchle";
                //לרחבת הכניסה של בניין פרניק
                break;
            }
            case "עיצוב תעשייתי": {
                path = "Mitchle/4";
                building = "Mitchle";
                break;
            }
            case "תקשורת חזותית": {
                path = "Mitchle/6";
                building = "Mitchle";
                //path = "Mitchle/5";
                break;
            }
            case "עיצוב פנים מבנה וסביבה": {
                path = "Mitchle/5";
                building = "Mitchle";
                break;
            }
            case "עיצוב טקסטיל": {

                path = "Pernik/-1";
                building = "Pernik";
                break;
            }
            case "אמנות רב תחומית": {
                path = "Mitchle/7";
                building = "Mitchle";
                // בבנין עלית ההיסטורי
                break;
            }
            case "הנדסת תוכנה": {
                path = "Pernik/2";
                building = "Pernik";
                // Shenkar.ac.il says Mitchle
                break;
            }
            case "עיצוב אופנה": {
                path = "Pernik/4";
                building = "Pernik";
                //  path = "Permik/3";
                break;
            }
            case "שנקר": {
                path = "Anna";
                building = "Shenkar";
                break;
            }
        }

        switch (building){
            case "Pernik" :{
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PERNIK, 20));
                AddMarker(location, department + " -  בניין פרניק");
            break;
            }
            case "Mitchle" :{
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MITSHLE,20));
                AddMarker(location, department + " - בניין מיטשל");
                break;
            }
            case "Shenkar" :
            default:{
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SHENKAR,18));
                AddMarker(location, department);
                break;
            }
        }

        setUpMap(path);
    }

    private void setUpMap(final String path) {


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public synchronized URL getTileUrl(int x, int y, int zoom) {
                // The moon tile coordinate system is reversed.  This is not normal.
                int reversedY = (1 << zoom) - y - 2;
                String s = String.format(Locale.US, "http://megastar.co.il/EPSG3857/"+ path +"/%d/%d/%d.png", zoom, x, y);
                URL url = null;
                try {
                    url = new URL(s);
                    //   System.out.println("Liron path url="+ url);
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
            //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
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

