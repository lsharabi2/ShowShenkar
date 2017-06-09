package il.ac.shenkar.endofyearshenkar.activities;

import il.ac.shenkar.endofyearshenkar.json.CollegeConfigJson;

/**
 * Created by User on 09/06/2017.
 */
public class StaticCollegeConfigJson {
    public static CollegeConfigJson mMainConfig;

    public static CollegeConfigJson getmMainConfig() {
        return mMainConfig;
    }

    public static void setmMainConfig(CollegeConfigJson mMainConfig) {
        StaticCollegeConfigJson.mMainConfig = mMainConfig;
    }
}
