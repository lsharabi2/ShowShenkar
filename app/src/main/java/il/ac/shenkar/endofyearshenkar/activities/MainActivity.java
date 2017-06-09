package il.ac.shenkar.endofyearshenkar.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.ArrayList;
import java.util.List;

import il.ac.shenkar.endofyearshenkar.R;
import il.ac.shenkar.endofyearshenkar.adapters.DepGridViewAdapter;
import il.ac.shenkar.endofyearshenkar.imagescache.ImageCache;
import il.ac.shenkar.endofyearshenkar.imagescache.ImageFetcher;
import il.ac.shenkar.endofyearshenkar.json.CollegeConfigJson;
import il.ac.shenkar.endofyearshenkar.json.DepartmentJson;
import il.ac.shenkar.endofyearshenkar.json.GsonRequest;
import il.ac.shenkar.endofyearshenkar.json.JsonURIs;

public class MainActivity extends ShenkarActivity {


    private static String TAG = "MAIN_ACTIVITY";
    private static String IMAGE_CACHE_DIR = "images";
    private GridView gridView;
    private DepGridViewAdapter gridAdapter;
    private List<DepartmentJson> mDepartments;
    private RequestQueue mRequestQueue;
    private ImageFetcher mImageFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fetch screen height and width, to use as our max size when loading images as this
        // activity runs full screen
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        // For this sample we'll use half of the longest width to resize our images. As the
        // image scaling ensures the image is larger than this, we should be left with a
        // resolution that is appropriate for both portrait and landscape. For best image quality
        // we shouldn't divide by 2, but this will use more memory and require a larger memory
        // cache.
        final int longest = (height > width ? height : width) / 2;

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(this, longest);
        mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
        mImageFetcher.setImageFadeIn(true);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mDepartments = new ArrayList<>();
        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new DepGridViewAdapter(this, R.layout.dep_grid_item_layout, mDepartments);
        gridView.setAdapter(gridAdapter);

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

        refreshCollegeConfigInfo();
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
        TextView logoText = (TextView) findViewById(R.id.logo_text);
        logoText.setText(config.getName());
        logoText.setTextColor(Color.parseColor(config.getMainTextColor()));

        ColorDrawable bgShape = (ColorDrawable) logoText.getBackground();
        bgShape.setColor(Color.parseColor(config.getPrimaryColor()));

        View mainLayout = findViewById(R.id.mlayout);
        bgShape = (ColorDrawable) mainLayout.getBackground();
        bgShape.setColor(Color.parseColor(config.getSecondaryColor()));

        ImageView logo_img = (ImageView) findViewById(R.id.toolbaricon);
        getImageFetcher().loadImage(config.getLogoUrl(), logo_img);
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
        mImageFetcher.setExitTasksEarly(false);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
    }

    public ImageFetcher getImageFetcher() {
        return mImageFetcher;
    }
}