package il.ac.shenkar.endofyearshenkarproject.json;

import java.util.ArrayList;
import java.util.List;

/**
 * ProjectJsonStatic hold the projects list
 */
public class ProjectJsonStatic {
    static private List<ProjectJson> projectJsonList = new ArrayList<>();

    public static List<ProjectJson> getProjectJsonList() {
        return projectJsonList;
    }

    public static void setProjectJsonList(List<ProjectJson> projectJsonList) {
        ProjectJsonStatic.projectJsonList = projectJsonList;
    }
}
