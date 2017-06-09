package il.ac.shenkar.endofyearshenkar.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import il.ac.shenkar.endofyearshenkar.R;
import il.ac.shenkar.endofyearshenkar.adapters.DepProjectsRecyclerAdapter;
import il.ac.shenkar.endofyearshenkar.json.ProjectJson;
import il.ac.shenkar.endofyearshenkar.json.RouteJson;
import il.ac.shenkar.endofyearshenkar.utils.DownloadImageTask;

public class SuggestedRouteActivity extends ShenkarActivity {

    private List<ProjectJson> mProjects;
    private DepProjectsRecyclerAdapter adapter;
    private Long mRouteId;
    private String mRouteName;
    private RouteJson mRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_route);

        mRouteId = getIntent().getLongExtra("id", 0);
        mRouteName = getIntent().getStringExtra("title");
        mRoute = (RouteJson) getIntent().getSerializableExtra("route");

        TextView titleTextView = (TextView) findViewById(R.id.SuggestedTitle);
        titleTextView.setText(mRouteName);

        // Initialize recycler view
        RecyclerView rvProjects = (RecyclerView) findViewById(R.id.projects);
        rvProjects.setLayoutManager(new LinearLayoutManager(this));

        mProjects = new ArrayList<>();

        adapter = new DepProjectsRecyclerAdapter(this, mProjects);
        rvProjects.setAdapter(adapter);
        initialize();
    }

    private void initialize() {
        if (StaticCollegeConfigJson.mMainConfig != null) {
            new DownloadImageTask((ImageView) findViewById(R.id.toolbaricon)).execute(StaticCollegeConfigJson.mMainConfig.getLogoUrl());
            TextView SuggestedTitle_Headline = (TextView) findViewById(R.id.SuggestedTitle);
            SuggestedTitle_Headline.setTextColor(Color.parseColor(StaticCollegeConfigJson.mMainConfig.getMainTextColor()));

            LinearLayout LLayout = (LinearLayout) findViewById(R.id.SuggestedRouteLayout);
            LLayout.setBackgroundColor(Color.parseColor(StaticCollegeConfigJson.mMainConfig.getSecondaryColor()));

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.refresh(mRoute.getProjectIds());
    }
}
