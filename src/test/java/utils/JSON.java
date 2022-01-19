package utils;
// JAVA
import java.util.ArrayList;
import java.util.List;
// JSON
import org.json.JSONArray;
import org.json.JSONObject;
// MINE
import models.Playlist;

public class JSON {
    public static List<Playlist> parse_JSONArray_to_ListPlaylists(JSONArray jsonArray) {
        // create return object
        List<Playlist> returnObject = new ArrayList<>();

        // if empty return returnObject
        if (jsonArray.isEmpty()) return returnObject;

        // loop objects, create new Playlist obj for each and assign to returnObject
        for (Object object : jsonArray) {
            // cast playlist to use JSONObject methods
            JSONObject jsObject = (JSONObject) object;
            // create Playlist object
            Playlist newPlaylist = new Playlist();
            newPlaylist.setId(String.valueOf(jsObject.getString("id")));
            newPlaylist.setName(jsObject.get("name").toString());
            newPlaylist.setDescription(jsObject.get("description").toString());
            newPlaylist.setUri(jsObject.get("uri").toString());
            // add to return object
            returnObject.add(newPlaylist);
        }
        return returnObject;
    }
}
