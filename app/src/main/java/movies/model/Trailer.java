package movies.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MOHAM on 12/09/2016.
 */
public class Trailer {
    private String id;
    private String key;
    private String name;


    public Trailer(JSONObject trailerObject) throws JSONException {
        this.id = trailerObject.getString("id");
        this.name = trailerObject.getString("name");
        this.key = trailerObject.getString("key");

    }

    public String getId() {
        return id;
    }
    public String getKey() {
        return key;
    }
    public String getName() {
        return name;
    }

}
