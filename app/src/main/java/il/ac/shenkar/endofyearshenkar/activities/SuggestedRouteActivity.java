package il.ac.shenkar.endofyearshenkar.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import il.ac.shenkar.endofyearshenkar.R;
import il.ac.shenkar.endofyearshenkar.adapters.DepProjectsRecyclerAdapter;
import il.ac.shenkar.endofyearshenkar.json.ProjectJson;

public class SuggestedRouteActivity extends ShenkarActivity {

    private List<ProjectJson> mProjects;
    private DepProjectsRecyclerAdapter adapter;
    private Long mRouteId;
    private String mRouteName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_route);

        mRouteId = getIntent().getLongExtra("id", 0);
        mRouteName = getIntent().getStringExtra("title");

        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(mRouteName);

        // Initialize recycler view
        RecyclerView rvProjects = (RecyclerView) findViewById(R.id.projects);
        rvProjects.setLayoutManager(new LinearLayoutManager(this));

        mProjects = new ArrayList<>();

        adapter = new DepProjectsRecyclerAdapter(this, mProjects);
        rvProjects.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.refresh(mRouteId);
    }
}
