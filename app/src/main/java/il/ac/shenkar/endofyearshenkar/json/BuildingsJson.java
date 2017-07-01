package il.ac.shenkar.endofyearshenkar.json;

/**
 * Created by User on 01/07/2017.
 */
public class BuildingsJson {

    private long id;
    private String name;
    private LocationJson location;
    private String locationDescription;
    private long zoom;

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

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public long getZoom() {
        return zoom;
    }

    public void setZoom(long zoom) {
        this.zoom = zoom;
    }

    public LocationJson getLocation() {
        return location;
    }

    public void setLocation(LocationJson location) {
        this.location = location;
    }
}