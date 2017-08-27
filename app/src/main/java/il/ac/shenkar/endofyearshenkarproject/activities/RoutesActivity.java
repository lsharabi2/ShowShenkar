package il.ac.shenkar.endofyearshenkarproject.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import il.ac.shenkar.endofyearshenkarproject.R;
import il.ac.shenkar.endofyearshenkarproject.adapters.RoutesRecyclerAdapter;
import il.ac.shenkar.endofyearshenkarproject.json.RouteJson;
import il.ac.shenkar.endofyearshenkarproject.utils.DownloadImageTask;

/**
 * This screen shows you favorite routes that was chosen by the institute and department managers
 */
public class RoutesActivity extends ShenkarActivity {

    private ArrayList<RouteJson> mRoutes;
    private RoutesRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        // Initialize recycler view
        RecyclerView rvProjects = (RecyclerView) findViewById(R.id.routes);
        rvProjects.setLayoutManager(new LinearLayoutManager(this));

        mRoutes = new ArrayList<>();
        adapter = new RoutesRecyclerAdapter(this, mRoutes);
        rvProjects.setAdapter(adapter);

        // Set screen views from StaticCollegeConfigJson which hold CollegeConfigJson information
        if (StaticCollegeConfigJson.mMainConfig != null) {
            new DownloadImageTask((ImageView) findViewById(R.id.toolbaricon)).execute(StaticCollegeConfigJson.mMainConfig.getLogoUrl());
            TextView Suggested_Route_Headline = (TextView) findViewById(R.id.RoutesHeadline);
            Suggested_Route_Headline.setTextColor(Color.parseColor(StaticCollegeConfigJson.mMainConfig.getMainTextColor()));

            LinearLayout LLayout = (LinearLayout) findViewById(R.id.RoutesLayout);
            LLayout.setBackgroundColor(Color.parseColor(StaticCollegeConfigJson.mMainConfig.getSecondaryColor()));

        }
    }

    @Override
    void setObjectID() {

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.refresh();
    }

    public void openRoutesRecyclerAdapter(View v) {
        Intent intent = new Intent(this, RoutesRecyclerAdapter.class);
        startActivity(intent);
    }
}