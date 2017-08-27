package il.ac.shenkar.endofyearshenkarproject.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import il.ac.shenkar.endofyearshenkarproject.R;
import il.ac.shenkar.endofyearshenkarproject.adapters.DepGridViewAdapter;
import il.ac.shenkar.endofyearshenkarproject.json.CollegeConfigJson;
import il.ac.shenkar.endofyearshenkarproject.json.DepartmentJson;
import il.ac.shenkar.endofyearshenkarproject.json.GsonRequest;
import il.ac.shenkar.endofyearshenkarproject.json.JsonURIs;
import il.ac.shenkar.endofyearshenkarproject.utils.NetworkUtil;

/**
 * College level screen
 */

public class MainActivity extends ShenkarActivity {


    private static String TAG = "MAIN_ACTIVITY";
    private static String IMAGE_CACHE_DIR = "images";
    private GridView gridView;
    private DepGridViewAdapter gridAdapter;
    private List<DepartmentJson> mDepartments;
    private RequestQueue mRequestQueue;
    private IntentFilter filter;
    private TextView logoText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initImageLoader(getBaseContext());

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mDepartments = new ArrayList<>();
        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new DepGridViewAdapter(this, R.layout.dep_grid_item_layout, mDepartments);
        gridAdapter.setSwipeRefreshLayout((SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout));
        gridView.setAdapter(gridAdapter);

        refreshCollegeConfigInfo();
    }


    /**
     * Set mobile's cache memory for faster loading from the second time you enter the app (will save data on mobile cache instead bringing it from the server first)
     * the information will be refreshed from the server after fiew seconds that the app is running unless no change has accrued
     */
    private void initImageLoader(Context baseContext) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(baseContext);
        config.defaultDisplayImageOptions(defaultOptions);
        config.diskCacheSize(50 * 1024 * 1024);

        ImageLoader.getInstance().init(config.build());

    }

    @Override
    void setObjectID() {
    }

    /**
     * Get CollegeConfig information from jason
     */
    private void refreshCollegeConfigInfo() {
        // Instantiate the RequestQueue.
        mRequestQueue = Volley.newRequestQueue(this);
        String url = JsonURIs.getConfigCollegeIdUri(JsonURIs.SHENKAR_COLLEGE_ID);

        GsonRequest<CollegeConfigJson> request = new GsonRequest<>(url, CollegeConfigJson.class, null,
                new Response.Listener<CollegeConfigJson>() {
                    @Override
                    public void onResponse(CollegeConfigJson response) {
                        System.out.println("Liron StaticCollegeConfigJson response =" + response.toString());
                        StaticCollegeConfigJson.mMainConfig = response;
                            update_views(StaticCollegeConfigJson.mMainConfig);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        request.setTag(TAG);
        // Add the request to the RequestQueue.
        mRequestQueue.add(request);

    }

    /**
     * Update all views for this screen
     */
    private void update_views(CollegeConfigJson config) {

        // logo text logic
        logoText = (TextView) findViewById(R.id.logo_text);
        logoText.setText(config.getName());
        logoText.setTextColor(Color.parseColor(config.getMainTextColor()));

        // app color logic
        ColorDrawable bgShape = (ColorDrawable) logoText.getBackground();
        bgShape.setColor(Color.parseColor(config.getPrimaryColor()));

        View mainLayout = findViewById(R.id.mlayout);
        bgShape = (ColorDrawable) mainLayout.getBackground();
        bgShape.setColor(Color.parseColor(config.getSecondaryColor()));

        // logo image changed by institute manager
        ImageView logo_img = (ImageView) findViewById(R.id.toolbaricon);
        ImageLoader.getInstance().displayImage(config.getLogoUrl(), logo_img);

        /**
         * When an item on the grid is pressed move to department activity and send data required
         */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                DepartmentJson item = (DepartmentJson) parent.getItemAtPosition(position);

                //Create intent
                Intent intent = new Intent(MainActivity.this, DepartmentActivity.class);
                intent.putExtra("title", item.getName());
                intent.putExtra("id", item.getId());
                intent.putExtra("location", item.getLocationDescription());
                intent.putExtra("image", item.getLargeImageUrl());

                //Start details activity
                startActivity(intent);

            }
        });


    }

    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }

    public void openRoutesActivity(View v) {
        Intent intent = new Intent(this, RoutesActivity.class);
        startActivity(intent);

    }

    public void openMyRouteActivity(View v) {
        Intent intent = new Intent(this, MyRouteActivity.class);
        startActivity(intent);

    }

    public void openGeneralActivity(View v) {
        Intent intent = new Intent(this, GeneralActivity.class);
        startActivity(intent);

    }


    @Override
    public void onResume() {
        super.onResume();
        gridAdapter.refresh();
        String status = NetworkUtil.getConnectivityStatusString(MainActivity.this);
        Toast.makeText(MainActivity.this, status, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        killBroadcast();
        System.exit(0);
    }

    /**
     * Finish broadcast on exit
     */
    private void killBroadcast() {
        PackageManager pm = MainActivity.this.getPackageManager();
        ComponentName componentName = new ComponentName(MainActivity.this, BroadcastReceiverClass.class);
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}