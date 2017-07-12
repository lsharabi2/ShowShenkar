package il.ac.shenkar.endofyearshenkarproject.json;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 05/04/2017.
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
