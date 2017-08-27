package il.ac.shenkar.endofyearshenkarproject.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import il.ac.shenkar.endofyearshenkarproject.R;
import il.ac.shenkar.endofyearshenkarproject.utils.DownloadImageTask;

/**
 * General information about the institute
 */
public class GeneralActivity extends ShenkarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        // set screen views from StaticCollegeConfigJson which hold CollegeConfigJson information and about's data (information and image)
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

    @Override
    void setObjectID() {

    }
}
