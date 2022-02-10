package tests.Playlists;
// JAVA
import java.util.ArrayList;
import java.util.List;
// JSON
import org.json.JSONObject;
// TEST-NG
import org.testng.Assert;
import org.testng.annotations.Test;
// MINE
import utils.restResources.RestfulPlaylist;
import models.Playlist;

public class ADD_ITEMS_TO_PLAYLIST extends Playlists {
    /*
        TEST : ADD ITEMS TO A PLAYLIST
        1 track added?
        multiple tracks added?
        no tracks added?
        Max tracks added?
            - 99
            - 100
            - 101
     */
    @Test
    public void addMultipleItemsToPlaylist() {
        // CREATE PLAYLIST
        Playlist res = RestfulPlaylist.createPlaylist(this.userId, Playlist.generatePlaylist());

        // GET URIs TO ADD TO PLAYLIST
        // get featured playlists
        List<Playlist> featuredPlaylists = RestfulPlaylist.getAllPlaylists_featured();
        // get ID of the first playlist
        String playlistId = featuredPlaylists.get(0).getId();
        // get the tracks of that playlist
        List<JSONObject> tracks = RestfulPlaylist.getPlaylistsTracks(playlistId);

        // CREATE LIST OF ITEMS TO ADD URIs TO
        List<String> itemsToAdd = new ArrayList<>();

        // ADD URIS FROM tracks TO itemsToAdd
        for (JSONObject track : tracks) {
            System.out.println("object in tracksItems: " + track.get("uri"));
            itemsToAdd.add(track.get("uri").toString());
        }

        // ADD TRACKS TO PLAYLIST
        RestfulPlaylist.addItemsToPlaylist(res.getId(), itemsToAdd);

        // GET TRACKS
        List<JSONObject> updatedTracks = RestfulPlaylist.getPlaylistsTracks(res.getId());

        // ASSERT
        // updateTracks has something
        Assert.assertTrue(updatedTracks.size() > 0);
        // updatedTracks has the same amount of tracks as the list of tracks to add
        Assert.assertEquals(updatedTracks.size(), tracks.size());
        for (int i = 0; i < updatedTracks.size(); i++) {
            System.out.println("updateTracks.get(i) " + updatedTracks.get(i));
            String uri = updatedTracks.get(i).get("uri").toString();
            Assert.assertEquals(uri, itemsToAdd.get(i));
        }
    }
}
