package il.ac.shenkar.endofyearshenkarproject.model;

public class DepImageItem {
    private int imageResource;
    private String title;

    public DepImageItem(int imageResource, String title) {
        super();
        this.imageResource = imageResource;
        this.title = title;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
