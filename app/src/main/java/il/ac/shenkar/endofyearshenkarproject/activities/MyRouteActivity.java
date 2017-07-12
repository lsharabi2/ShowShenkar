package il.ac.shenkar.endofyearshenkarproject.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import il.ac.shenkar.endofyearshenkarproject.R;
import il.ac.shenkar.endofyearshenkarproject.adapters.DepProjectsRecyclerAdapter;
import il.ac.shenkar.endofyearshenkarproject.json.ProjectJson;
import il.ac.shenkar.endofyearshenkarproject.utils.DownloadImageTask;

public class MyRouteActivity extends ShenkarActivity {
    private List<ProjectJson> mProjects;

    private DepProjectsRecyclerAdapter adapter;
    private RecyclerView rvProjects;
    private TextView emptyView;

    //  private CollegeConfigJson mMainConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_route);


        // Initialize recycler view
        rvProjects = (RecyclerView) findViewById(R.id.projects);
        rvProjects.setLayoutManager(new LinearLayoutManager(this));

        emptyView = (TextView) findViewById(R.id.no_projects);
        mProjects = new ArrayList<>();
        adapter = new DepProjectsRecyclerAdapter(this, mProjects);
        rvProjects.setAdapter(adapter);

        //  StaticCollegeConfigJson.mMainConfig = (CollegeConfigJson) getIntent().getSerializableExtra("main_config");

        if (StaticCollegeConfigJson.mMainConfig != null) {
            new DownloadImageTask((ImageView) findViewById(R.id.toolbaricon)).execute(StaticCollegeConfigJson.mMainConfig.getLogoUrl());
            TextView MyRoute_Headline = (TextView) findViewById(R.id.MyRouteHeadline);
            MyRoute_Headline.setTextColor(Color.parseColor(StaticCollegeConfigJson.mMainConfig.getMainTextColor()));

            // ColorDrawable bgShape = (ColorDrawable) MyRoute_Headline.getBackground();
            // bgShape.setColor(Color.parseColor(mMainConfig.getPrimaryColor()));

            LinearLayout LLayout = (LinearLayout) findViewById(R.id.MyRouteLayout);
            LLayout.setBackgroundColor(Color.parseColor(StaticCollegeConfigJson.mMainConfig.getSecondaryColor()));

        }
    }

    @Override
    void setObjectID() {

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.clear();

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), MODE_PRIVATE);
        Set<String> projectIds = sharedPref.getStringSet(getString(R.string.preference_ids_key), new HashSet<String>());

        if (projectIds == null || projectIds.isEmpty()) {
            rvProjects.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            List<Long> projectIdsLong = new ArrayList<Long>();
            for (String idString : projectIds) {
                projectIdsLong.add(Long.parseLong(idString));
            }

            adapter.refresh(projectIdsLong);
            rvProjects.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}