package movies.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MOHAM on 12/09/2016.
 */
public class Review {
    private String id;
    private String author;
    private String content;


    public Review(JSONObject reviewObject) throws JSONException {
        this.id = reviewObject.getString("id");
        this.author = reviewObject.getString("author");
        this.content = reviewObject.getString("content");

    }


    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }
}
