package il.ac.shenkar.endofyearshenkar.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import il.ac.shenkar.endofyearshenkar.R;
import il.ac.shenkar.endofyearshenkar.utils.DownloadImageTask;
import il.ac.shenkar.endofyearshenkar.adapters.DepProjectsRecyclerAdapter;
import il.ac.shenkar.showshenkar.backend.departmentApi.model.Department;
import il.ac.shenkar.showshenkar.backend.projectApi.model.Project;


public class DepartmentActivity extends ShenkarActivity {
    private List<Project> mProjects;
    private Department mDepartment;
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
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.refresh(mDepartmentName);
    }

    public void showDepartmentLocation(View v) {
        Intent i = new Intent(this, MapActivity.class);
        i.putExtra("objectId", mDepartmentId);
        i.putExtra("objectType", "department");
        startActivity(i);
    }

}