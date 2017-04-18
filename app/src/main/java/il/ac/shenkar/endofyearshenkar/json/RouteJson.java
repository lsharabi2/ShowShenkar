package il.ac.shenkar.endofyearshenkar.json;

import java.util.List;

/**
 * Created by User on 05/04/2017.
 */
public class RouteJson {
    private String name;
    private List<Long> projectIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<Long> projectIds) {
        this.projectIds = projectIds;
    }
}
