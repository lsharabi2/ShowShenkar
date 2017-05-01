package il.ac.shenkar.endofyearshenkar.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import il.ac.shenkar.endofyearshenkar.R;
import il.ac.shenkar.endofyearshenkar.json.CollegeConfigJson;
import il.ac.shenkar.endofyearshenkar.utils.DownloadImageTask;


public class GeneralActivity extends ShenkarActivity {

    private CollegeConfigJson mMainConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        mMainConfig = (CollegeConfigJson) getIntent().getSerializableExtra("main_config");

        if (mMainConfig != null) {
            TextView generalInfo = (TextView) findViewById(R.id.txtGeneralInfo);
            generalInfo.setText(mMainConfig.getAboutText());
            new DownloadImageTask((ImageView) findViewById(R.id.alldepartments)).execute(mMainConfig.getAboutImageUrl());
        }
    }
}
