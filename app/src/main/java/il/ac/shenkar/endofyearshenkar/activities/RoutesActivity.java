package il.ac.shenkar.endofyearshenkar.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import il.ac.shenkar.endofyearshenkar.R;
import il.ac.shenkar.endofyearshenkar.adapters.RoutesRecyclerAdapter;
import il.ac.shenkar.endofyearshenkar.json.RouteJson;

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
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.refresh();
    }
}