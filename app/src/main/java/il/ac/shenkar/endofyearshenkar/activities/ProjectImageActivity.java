package il.ac.shenkar.endofyearshenkar.activities;

import android.os.Bundle;
import android.widget.ImageView;

import il.ac.shenkar.endofyearshenkar.R;
import il.ac.shenkar.endofyearshenkar.utils.DownloadImageTask;

public class ProjectImageActivity extends ShenkarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_image);

        String imageUrl = getIntent().getStringExtra("url");

        new DownloadImageTask((ImageView)findViewById(R.id.image)).execute(imageUrl);
    }
}
