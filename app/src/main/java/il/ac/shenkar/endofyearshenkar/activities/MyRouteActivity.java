package il.ac.shenkar.endofyearshenkar.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import il.ac.shenkar.endofyearshenkar.R;
import il.ac.shenkar.endofyearshenkar.adapters.DepProjectsRecyclerAdapter;

import il.ac.shenkar.showshenkar.backend.projectApi.model.Project;

public class MyRouteActivity extends ShenkarActivity {
    private List<Project> mProjects;

    private DepProjectsRecyclerAdapter adapter;
    private RecyclerView rvProjects;
    private TextView emptyView;

    public static void addProjectId(Context context, Long projectId) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Set<String> projectIdsStr = new HashSet<>(sharedPref.getStringSet(context.getString(R.string.preference_ids_key), new HashSet<String>()));
        projectIdsStr.add(Long.toString(projectId));

        editor.putStringSet(context.getString(R.string.preference_ids_key), projectIdsStr);
        editor.commit();
    }

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
            adapter.refresh(projectIds);
            rvProjects.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}