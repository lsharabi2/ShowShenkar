package il.ac.shenkar.endofyearshenkar.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import il.ac.shenkar.endofyearshenkar.R;
import il.ac.shenkar.endofyearshenkar.utils.DownloadImageTask;


public class GeneralActivity extends ShenkarActivity {

    // private CollegeConfigJson mMainConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        // mMainConfig = (CollegeConfigJson) getIntent().getSerializableExtra("main_config");

        if (StaticCollegeConfigJson.mMainConfig != null) {
            TextView generalInfo = (TextView) findViewById(R.id.txtGeneralInfo);
            generalInfo.setText(StaticCollegeConfigJson.mMainConfig.getAboutText());
            new DownloadImageTask((ImageView) findViewById(R.id.alldepartments)).execute(StaticCollegeConfigJson.mMainConfig.getAboutImageUrl());
            new DownloadImageTask((ImageView) findViewById(R.id.toolbaricon)).execute(StaticCollegeConfigJson.mMainConfig.getLogoUrl());

            TextView general_Headline = (TextView) findViewById(R.id.generalHeadline);
            general_Headline.setTextColor(Color.parseColor(StaticCollegeConfigJson.mMainConfig.getMainTextColor()));

            ScrollView scroll = (ScrollView) findViewById(R.id.generallayout);
            scroll.setBackgroundColor(Color.parseColor(StaticCollegeConfigJson.mMainConfig.getSecondaryColor()));


        }
    }
}
