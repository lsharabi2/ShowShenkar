package il.ac.shenkar.endofyearshenkarproject.activities;

import il.ac.shenkar.endofyearshenkarproject.json.CollegeConfigJson;

/**
 * that class represent the College Json information which we need for the general look of each screen
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
