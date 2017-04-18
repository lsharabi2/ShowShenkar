package il.ac.shenkar.endofyearshenkar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.ArrayList;
import java.util.List;

import il.ac.shenkar.endofyearshenkar.R;
import il.ac.shenkar.endofyearshenkar.adapters.DepGridViewAdapter;
import il.ac.shenkar.endofyearshenkar.json.DepartmentJson;

public class MainActivity extends ShenkarActivity {

    private GridView gridView;
    private DepGridViewAdapter gridAdapter;
    private List<DepartmentJson> mDepartments;

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
        startActivity(intent);
    }
}