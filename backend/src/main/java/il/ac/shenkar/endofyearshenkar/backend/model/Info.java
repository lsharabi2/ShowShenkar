package il.ac.shenkar.endofyearshenkar.backend.model;

import com.googlecode.objectify.annotation.Embed;

import java.io.Serializable;

/**
 * Created by:  Gregory Kondratenko on 10/06/2016.
 * Description: Info entity class for app content
 */
@Embed
public class Info implements Serializable {
    String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
