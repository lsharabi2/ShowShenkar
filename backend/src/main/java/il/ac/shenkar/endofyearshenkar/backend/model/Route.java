package il.ac.shenkar.endofyearshenkar.backend.model;

import com.googlecode.objectify.annotation.Entity;

import java.util.List;

/**
 * Created by:  Gregory Kondratenko on 11/06/2016.
 * Description: Route entity class
 */
@Entity
public class Route extends BaseEntity {
    String name;
    List<String> projectIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<String> projectIds) {
        this.projectIds = projectIds;
    }
}
