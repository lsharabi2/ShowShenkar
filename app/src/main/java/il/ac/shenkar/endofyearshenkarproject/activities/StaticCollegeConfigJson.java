package il.ac.shenkar.endofyearshenkarproject.activities;

import il.ac.shenkar.endofyearshenkarproject.json.CollegeConfigJson;

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
