package il.ac.shenkar.endofyearshenkar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

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
import il.ac.shenkar.endofyearshenkar.json.CollegeConfigJson;
import il.ac.shenkar.endofyearshenkar.json.DepartmentJson;
import il.ac.shenkar.endofyearshenkar.json.GsonRequest;
import il.ac.shenkar.endofyearshenkar.json.JsonURIs;

public class MainActivity extends ShenkarActivity {

    private static String TAG = "MAIN_ACTIVITY";
    private GridView gridView;
    private DepGridViewAdapter gridAdapter;
    private List<DepartmentJson> mDepartments;
    private RequestQueue mRequestQueue;
    private CollegeConfigJson mMainConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        mMainConfig = response;
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

    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }

    @Override
    public void onResume() {
        super.onResume();
        gridAdapter.refresh();
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
        intent.putExtra("main_config", mMainConfig);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }
}