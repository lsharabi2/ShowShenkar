package il.ac.shenkar.endofyearshenkar.backend.model;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by:  Gregory Kondratenko on 10/06/2016.
 * Description: Base Entity class for app content
 */
public class BaseEntity implements Serializable {

    @Id
    Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
