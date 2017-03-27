package il.ac.shenkar.endofyearshenkar.backend.model;

import com.googlecode.objectify.annotation.Embed;

import java.io.Serializable;

/**
 * Created by:  Gregory Kondratenko on 10/06/2016.
 * Description: Location entity class for app content
 */
@Embed
public class Location implements Serializable {
    String description;
    Long lat;
    Long lng;
    String url;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLat() {
        return lat;
    }

    public void setLat(Long lat) {
        this.lat = lat;
    }

    public Long getLng() {
        return lng;
    }

    public void setLng(Long lng) {
        this.lng = lng;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
