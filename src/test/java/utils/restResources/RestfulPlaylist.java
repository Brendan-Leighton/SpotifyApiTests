package utils.restResources;
// JAVA

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// REST-ASSURED
import io.restassured.response.Response;
// JSON
import org.json.JSONArray;
import org.json.JSONObject;
// MINE
import models.Playlist;
import utils.Endpoints;


/**
 * Logic for dealing with Spotify's endpoints related to Playlists
 */
public class RestfulPlaylist {

    //*************************
    //*************************
    //          GET
    //*************************
    //*************************

    /**
     * GET all playlists in the "featured" section from the browse page.
     *
     * @return example...
     * <br/>{
     * <br/>_  "albums": {
     * <br/>_ _    "href": "https://api.spotify.com/v1/me/shows?offset=0&limit=20\n",
     * <br/>_ _    "items": [
     * <br/>_ _ _      {}
     * <br/>_ _    ],
     * <br/>_ _    "limit": 20,
     * <br/>_ _    "next": "https://api.spotify.com/v1/me/shows?offset=1&limit=1",
     * <br/>_ _    "offset": 0,
     * <br/>_ _    "previous": "https://api.spotify.com/v1/me/shows?offset=1&limit=1",
     * <br/>_ _    "total": 4
     * <br/>_  },
     * <br/>  "message": "string"
     * <br/>}
     */
    public static Response getAllPlaylists_featured() {
        return RestResource.get("browse/featured-playlists");
    }

    public static List<Playlist> getAllPlaylists_forSingleUser(String userId) {
        // get playlists
        String response = RestResource.get(Endpoints.USERS + userId + '/' + Endpoints.PLAYLISTS).asString();

        // parse response
        JSONObject json = new JSONObject(response);
        JSONArray playlists = json.getJSONArray("items");

        // create return object
        List<Playlist> returnObject = new ArrayList<>();

        // if no playlists return returnObject
        if (playlists.isEmpty()) return returnObject;

        // loop playlists, create new Playlist obj for each and assign to returnObject
        for (Object playlist : playlists) {
            // cast playlist to use JSONObject methods
            JSONObject jsObject = (JSONObject) playlist;
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

    /**
     * GET a playlist by a playlists ID
     *
     * @param playlistId ID of the playlist you want
     * @return Playlist object as a Response object
     */
    public static Playlist getPlaylist_byId(String playlistId) {
        return RestResource.get(Endpoints.PLAYLISTS + '/' + playlistId).as(Playlist.class);
    }

    public static List<Map<String, String>> getPlaylistsTracks(String playlistId) {
        // GET TRACKS
        Response res = RestResource.get(Endpoints.PLAYLISTS + '/' + playlistId + '/' + Endpoints.TRACKS);

        // PARSE RES
        JSONObject json = new JSONObject(res);
        JSONArray playlists = json.getJSONArray("items");

        List<Map<String, String>> tracks = new ArrayList<>();
        if (playlists.isEmpty()) return tracks;
        // loop tracks, create new Track obj for each and assign to tracks
//        for (Object playlist : playlists) {
//            // cast playlist to use JSONObject methods
//            JSONObject jsObject = (JSONObject) playlist;
//            // create Playlist object
//            Map<String, String> newTrack = new HashMap<>();
//            newTrack.put("id", String.valueOf(jsObject.getString("id")));
//            newTrack.put("name", jsObject.get("name").toString());
//            // add to return object
//            tracks.add(newTrack);
//        }
        return tracks;
    }

    //*************************
    //*************************
    //         DELETE
    //*************************
    //*************************

    /**
     * Simulates deleting a playlist. There is no endpoint to delete a playlist. When a user logs into Spotify and manually deletes a playlist it's not actually deleted but rather the playlist is unfollowed.
     *
     * @param playlistId id of the playlist to be deleted/unfollowed
     * @return nothing is returned from the endpoint on a successful DELETE. Unsuccessful request return
     * <br/>{
     * <br/>_   "error": {
     * <br/>_ _   "status": 400,
     * <br/>_ _   "message": "string"
     * <br/>  } }
     */
    public static void deletePlaylist_byId(String playlistId) {
        RestResource.delete(Endpoints.PLAYLISTS + '/' + playlistId + "/followers");
    }

    //*************************
    //*************************
    //          POST
    //*************************
    //*************************

    /**
     * Create a playlist for the specified user
     *
     * @param userId   ID of the user who wants a new playlist
     * @param playlist The Playlist object to be created
     * @return returns the newly created playlist as a Playlist object
     */
    public static Playlist createPlaylist(String userId, Playlist playlist) {
        return RestResource.post(Endpoints.USERS + userId + '/' + Endpoints.PLAYLISTS, playlist).as(Playlist.class);
    }
}
