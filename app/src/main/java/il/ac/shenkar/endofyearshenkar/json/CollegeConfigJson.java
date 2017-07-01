package il.ac.shenkar.endofyearshenkar.json;

import java.io.Serializable;

public class CollegeConfigJson implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private String logoUrl;
    private String primaryColor;
    private String secondaryColor;
    private String lineColor;
    private String mainTextColor;
    private String aboutText;
    private String aboutImageUrl;
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getLineColor() {
        return lineColor;
    }

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public String getMainTextColor() {
        return mainTextColor;
    }

    public void setMainTextColor(String mainTextColor) {
        this.mainTextColor = mainTextColor;
    }

    public String getAboutText() {
        return aboutText;
    }

    public void setAboutText(String aboutText) {
        this.aboutText = aboutText;
    }

    public String getAboutImageUrl() {
        return aboutImageUrl;
    }

    public void setAboutImageUrl(String aboutImageUrl) {
        this.aboutImageUrl = aboutImageUrl;
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
