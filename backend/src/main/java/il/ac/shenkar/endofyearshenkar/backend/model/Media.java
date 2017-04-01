package il.ac.shenkar.endofyearshenkar.backend.model;

import com.googlecode.objectify.annotation.Embed;

import java.io.Serializable;


/**
 * Created by:  Gregory Kondratenko on 10/06/2016.
 * Description: Media entity class for app content
 */
@Embed
public class Media implements Serializable {

    String type;
    String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
