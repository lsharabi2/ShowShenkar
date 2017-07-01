package il.ac.shenkar.endofyearshenkar.json;

import java.io.Serializable;

public class DepartmentJson implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private String imageUrl;
    private String largeImageUrl;
    private String locationDescription;
    private LocationJson location;
    private String path;
    private BuildingsJson building;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public LocationJson getLocation() {
        return location;
    }

    public void setLocation(LocationJson location) {
        this.location = location;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public BuildingsJson getBuilding() {
        return building;
    }

    public void setBuilding(BuildingsJson building) {
        this.building = building;
    }
}

