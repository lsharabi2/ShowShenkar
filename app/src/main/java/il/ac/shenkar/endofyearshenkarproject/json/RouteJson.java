package il.ac.shenkar.endofyearshenkarproject.json;

import java.io.Serializable;
import java.util.List;

/**
 * routes Json, hold the routes list of projects
 */
public class RouteJson implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
