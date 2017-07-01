package il.ac.shenkar.endofyearshenkar.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import il.ac.shenkar.endofyearshenkar.R;
import il.ac.shenkar.endofyearshenkar.adapters.DepGridViewAdapter;
import il.ac.shenkar.endofyearshenkar.json.CollegeConfigJson;
import il.ac.shenkar.endofyearshenkar.json.DepartmentJson;
import il.ac.shenkar.endofyearshenkar.json.GsonRequest;
import il.ac.shenkar.endofyearshenkar.json.JsonURIs;
import il.ac.shenkar.endofyearshenkar.utils.NetworkUtil;

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
        gridView.setAdapter(gridAdapter);

//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                DepartmentJson item = (DepartmentJson) parent.getItemAtPosition(position);
//
//                //Create intent
//                Intent intent = new Intent(MainActivity.this, DepartmentActivity.class);
//                intent.putExtra("title", item.getName());
//                intent.putExtra("id", item.getId());
//                intent.putExtra("location", item.getLocationDescription());
//                intent.putExtra("image", item.getLargeImageUrl());
//
//                //Start details activity
//                startActivity(intent);
//            }
//        });


        refreshCollegeConfigInfo();
    }


    private void initImageLoader(Context baseContext) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(baseContext);
        config.defaultDisplayImageOptions(defaultOptions);
        config.diskCacheSize(50 * 1024 * 1024); //50MiB

        ImageLoader.getInstance().init(config.build());


        //if neded add

    }

    @Override
    void setObjectID() {

    }

    private void refreshCollegeConfigInfo() {
        // Instantiate the RequestQueue.
        mRequestQueue = Volley.newRequestQueue(this);
        String url = JsonURIs.getConfigCollegeIdUri(JsonURIs.SHENKAR_COLLEGE_ID);

        GsonRequest request = new GsonRequest(url, CollegeConfigJson.class, null,
                new Response.Listener<CollegeConfigJson>() {
                    @Override
                    public void onResponse(CollegeConfigJson response) {
                        StaticCollegeConfigJson.mMainConfig = response;
                        if (StaticCollegeConfigJson.mMainConfig != null) {
                            update_views(StaticCollegeConfigJson.mMainConfig);
                        }
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

    private void update_views(CollegeConfigJson config) {
        logoText = (TextView) findViewById(R.id.logo_text);
        logoText.setText(config.getName());
        logoText.setTextColor(Color.parseColor(config.getMainTextColor()));

        ColorDrawable bgShape = (ColorDrawable) logoText.getBackground();
        bgShape.setColor(Color.parseColor(config.getPrimaryColor()));

        View mainLayout = findViewById(R.id.mlayout);
        bgShape = (ColorDrawable) mainLayout.getBackground();
        bgShape.setColor(Color.parseColor(config.getSecondaryColor()));

        ImageView logo_img = (ImageView) findViewById(R.id.toolbaricon);
        ImageLoader.getInstance().displayImage(config.getLogoUrl(), logo_img);


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
        //   intent.putExtra("main_config", mMainConfig);
        startActivity(intent);
    }

    public void openMyRouteActivity(View v) {
        Intent intent = new Intent(this, MyRouteActivity.class);
        //  intent.putExtra("main_config", mMainConfig);
        startActivity(intent);
    }

    public void openGeneralActivity(View v) {
        Intent intent = new Intent(this, GeneralActivity.class);
        //   intent.putExtra("main_config", mMainConfig);
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
}