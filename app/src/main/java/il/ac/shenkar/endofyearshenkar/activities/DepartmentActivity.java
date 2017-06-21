package il.ac.shenkar.endofyearshenkar.activities;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import il.ac.shenkar.endofyearshenkar.R;
import il.ac.shenkar.endofyearshenkar.adapters.DepProjectsRecyclerAdapter;
import il.ac.shenkar.endofyearshenkar.json.ProjectJson;
import il.ac.shenkar.endofyearshenkar.utils.DownloadImageTask;


public class DepartmentActivity extends ShenkarActivity {
    private static String TAG = "DepartmentActivity";
    private List<ProjectJson> mProjects;
    private Long mDepartmentId;
    private String mDepartmentName;
    private DepProjectsRecyclerAdapter adapter;
    private ImageButton buttonWhere;
    private AlertDialog.Builder dialog;
    private String mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        mDepartmentId = getIntent().getLongExtra("id", 0);
        mDepartmentName = getIntent().getStringExtra("title");
        mLocation = getIntent().getStringExtra("location");
        buttonWhere = (ImageButton) findViewById(R.id.btnLocation);
        final String imageUrl = getIntent().getStringExtra("image");

        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(mDepartmentName);

        TextView locationTextView = (TextView) findViewById(R.id.location);
        locationTextView.setText(mLocation);

        final ImageView imageView = (ImageView) findViewById(R.id.image);
        new DownloadImageTask(imageView).execute(imageUrl);

        // Initialize recycler view
        RecyclerView rvProjects = (RecyclerView) findViewById(R.id.projects);
        rvProjects.setLayoutManager(new LinearLayoutManager(this));

        mProjects = new ArrayList<>();

        adapter = new DepProjectsRecyclerAdapter(this, mProjects);
        rvProjects.setAdapter(adapter);


        SearchView activity_department_search = (SearchView) findViewById(R.id.activity_department_search);
        activity_department_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        initialize();

        setObjectID();
    }


    private void initialize() {
        if (StaticCollegeConfigJson.mMainConfig != null) {
            new DownloadImageTask((ImageView) findViewById(R.id.toolbaricon)).execute(StaticCollegeConfigJson.mMainConfig.getLogoUrl());
            TextView title_Headline = (TextView) findViewById(R.id.title);
            title_Headline.setTextColor(Color.parseColor(StaticCollegeConfigJson.mMainConfig.getMainTextColor()));

            TextView location_Headline = (TextView) findViewById(R.id.location);
            location_Headline.setTextColor(Color.parseColor(StaticCollegeConfigJson.mMainConfig.getMainTextColor()));

            LinearLayout LLayout = (LinearLayout) findViewById(R.id.DepartmentLayout);
            LLayout.setBackgroundColor(Color.parseColor(StaticCollegeConfigJson.mMainConfig.getSecondaryColor()));

        }
    }




    @Override
    public void onResume() {
        super.onResume();
        adapter.refresh(mDepartmentId);
    }

//    public void showDepartmentLocation(View v) {
//
//        System.out.println("Liron showDepartmentLocation ");
//
//        Intent i = new Intent(this, MapActivity.class);
//        i.putExtra("objectId", mDepartmentId);
//        i.putExtra("objectType", "department");
//        startActivity(i);
//    }


    @Override
    void setObjectID() {
        System.out.println("Liron  setObjectID mDepartmentId =" + mDepartmentId);
        this.objectId = mDepartmentId;
        this.objectType = "department";
    }

}