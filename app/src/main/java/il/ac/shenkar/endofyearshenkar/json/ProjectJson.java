package il.ac.shenkar.endofyearshenkar.json;

import java.util.List;

public class ProjectJson {
    private long id;
    private long institute;
    private long departmentId;
    private String name;
    private String description;
    private List<String> studentNames;
    private List<String> studentEmails;
    private String videoUrl;
    private String soundUrl;
    //private LocationJson location;
    private List<String> imageUrl;

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getStudentNames() {
        return studentNames;
    }

    public void setStudentNames(List<String> studentNames) {
        this.studentNames = studentNames;
    }

    public List<String> getStudentEmails() {
        return studentEmails;
    }

    public void setStudentEmails(List<String> studentEmails) {
        this.studentEmails = studentEmails;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getSoundUrl() {
        return soundUrl;
    }

    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }

    //public LocationJson getLocation() {
    //    return location;
    //}

    //public void setLocation(LocationJson location) {
    //    this.location = location;
    //}

    public List<String> getImagesUrls() {
        return imageUrl;
    }

    public void setImagesUrls(List<String> imagesUrls) {
        this.imageUrl = imagesUrls;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInstitute() {
        return institute;
    }

    public void setInstitute(long institute) {
        this.institute = institute;
    }
}