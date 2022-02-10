package tests.Playlists;

import models.Playlist;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.restResources.RestfulPlaylist;

import java.util.ArrayList;
import java.util.List;

/**
 * TEST : GET PLAYLISTS' ITEMS
 *
 * Test
 *    - not empty playlist
 *    - empty playlist
 */
public class GET_PLAYLIST_ITEMS extends Playlists{

    /**
     * GET items from a non-empty playlist
     */
    @Test
    public void getPlaylistItems_NotEmpty() {
        // CREATE PLAYLIST
        // create request body
        Playlist playlist = new Playlist();
        playlist.setName("Get me by my ID! " + System.currentTimeMillis());
        playlist.setDescription("Made to be gotten");
        playlist.setPublic(false);
        // send request
        Playlist res = RestfulPlaylist.createPlaylist(userId, playlist);
        // get id
        String playlistId = res.getId();

        // ADD TRACK
        List<Playlist> featuredPlaylists = RestfulPlaylist.getAllPlaylists_featured();
        JSONObject trackThatWasAdded = RestfulPlaylist.getPlaylistsTracks(featuredPlaylists.get(0).getId()).get(0);
        String songUri = trackThatWasAdded.get("uri").toString();
        List<String> newItems = new ArrayList<>();
        newItems.add(songUri);
        RestfulPlaylist.addItemsToPlaylist(playlistId, newItems);

        // TEST FUNCTION
        List<JSONObject> tracksToCheck = RestfulPlaylist.getPlaylistsTracks(playlistId);

        // ASSERT
        // has an item
        Assert.assertEquals(tracksToCheck.size(), 1);
        // has correct item
        Assert.assertEquals(tracksToCheck.get(0).get("name"), trackThatWasAdded.get("name"));
    }
}