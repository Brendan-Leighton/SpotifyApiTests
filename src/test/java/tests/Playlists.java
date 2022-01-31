package tests;
// JAVA
import java.util.ArrayList;
import java.util.List;
// TEST-NG
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
// JSON
import org.json.JSONObject;
// REST-ASSURED
import io.restassured.response.Response;
// MINE
import utils.Endpoints;
import utils.restResources.RestResource;
import utils.restResources.RestfulPlaylist;
import models.Playlist;


/**
 * Tests pertaining to the "playlist" endpoint for Spotify's API
 */
public class Playlists {

    private String userId;

    @BeforeTest
    public void setup() {
        // GET USER's ID
        // send request
        Response userData = RestResource.get(Endpoints.ME);
        // extracting users' ID
        userId = userData.path("id");
    }

    //*************************
    //*************************
    //      POST TESTS
    //*************************
    //*************************

    @Test
    public void createPlaylist() {

        // CREATE THE PLAYLIST
        // create request body
        Playlist playlist = new Playlist();
        playlist.setName("New Playlist " + System.currentTimeMillis());
        playlist.setDescription("Auto generated");
        playlist.setPublic(false);
        // send request
        Playlist res = RestfulPlaylist.createPlaylist(userId, playlist);

        // ASSERT
        Assert.assertEquals(res.getName(), playlist.getName());
    }

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
        Playlist res = RestfulPlaylist.createPlaylist(userId, Playlist.generatePlaylist());

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

    //*************************
    //*************************
    //       GET TESTS
    //*************************
    //*************************

    /*
        TEST : GET PLAYLISTS
        - has playlists
        - has no playlists
     */

    /**
     * GET all playlists belonging to a user
     */
    @Test
    public void getUsersPlaylists_hasPlaylists() {
        // ENSURE USER HAS 0 PLAYLISTS
        RestfulPlaylist.deleteAllPlaylists_forSingleUser(this.userId);

        // CREATE A PLAYLIST TO ADD
        Playlist playlistToAdd = Playlist.generatePlaylist();

        // ADD PLAYLIST
        RestfulPlaylist.createPlaylist(this.userId, playlistToAdd);

        // GET ALL PLAYLISTS FOR USER
        List<Playlist> resultingPlaylists = RestfulPlaylist.getAllPlaylists_forSingleUser(this.userId);

        // ASSERT
        // user has 1 playlist
        Assert.assertEquals(resultingPlaylists.size(), 1);
        // playlistId matches added playlist
        Assert.assertEquals(resultingPlaylists.get(0).getName(), playlistToAdd.getName());
    }

    /**
     * GET one playlist by its ID
     */
    @Test
    public void getPlaylistByPlaylistId() {
        // CREATE PLAYLIST
        // create request body
        Playlist playlist = new Playlist();
        playlist.setName("Get me by my ID! " + System.currentTimeMillis());
        playlist.setDescription("Made to be gotten");
        playlist.setPublic(false);
        // send request
        Playlist res = RestfulPlaylist.createPlaylist(userId, playlist);


        // GET PLAYLIST BY ID
        Playlist playlist_gotById = RestfulPlaylist.getPlaylist_byId(res.getId());

        // ASSERT
        Assert.assertEquals(playlist.getName(), playlist_gotById.getName());
        Assert.assertEquals(playlist.getDescription(), playlist_gotById.getDescription());
        Assert.assertEquals(playlist.getPublic(), playlist_gotById.getPublic());
    }

    /*

        TEST : GET PLAYLISTS' ITEMS
        - not empty playlist
        - empty playlist

     */

    /**
     * GET items from a playlist.
     *
     * "items" could be songs, podcasts, etc.
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

    @Test
    public void getFeaturedPlaylists() {
        List<Playlist> playlists = RestfulPlaylist.getAllPlaylists_featured();
        Assert.assertTrue(playlists.size() > 0);
    }

    //*************************
    //*************************
    //      DELETE TESTS
    //*************************
    //*************************

    /**
     * DELETE a playlist by its ID.
     *
     * Spotify doesn't actually delete playlists just makes the user unfollow it. So, by "delete" we always mean unfollow. It will no longer show up the in the users' list of playlists
     */
    @Test
    public void deletePlaylistById() {

        // GET PLAYLISTS
        List<Playlist> res_usersPlaylists_beforeDel = RestfulPlaylist.getAllPlaylists_forSingleUser(this.userId);

        // if there are no playlists, CREATE a PLAYLIST
        if (res_usersPlaylists_beforeDel.size() == 0) {
            // create request body
            Playlist playlist = new Playlist();
            playlist.setName("New Playlist " + System.currentTimeMillis());
            playlist.setDescription("Auto generated");
            playlist.setPublic(false);
            // send request
            RestfulPlaylist.createPlaylist(userId, playlist);
            // get playlists
            res_usersPlaylists_beforeDel = RestfulPlaylist.getAllPlaylists_forSingleUser(this.userId);
        }

        // GET ID OF A PLAYLIST
        String playlistId = res_usersPlaylists_beforeDel.get(0).getId();

        // DELETE PLAYLIST
        RestfulPlaylist.deletePlaylist_byId(playlistId);

        // GET PLAYLISTS
        List<Playlist> res_usersPlaylists_afterDel = RestfulPlaylist.getAllPlaylists_forSingleUser(this.userId);

        // ASSERT
        // expect 1 less playlist after delete
        Assert.assertEquals(res_usersPlaylists_beforeDel.size() - 1, res_usersPlaylists_afterDel.size());
        // expect ID of deleted playlist to not be in list
        if (res_usersPlaylists_afterDel.size() != 0) {
            boolean isPlaylistDeleted = true;
            for (Playlist playlist : res_usersPlaylists_afterDel) {
                if (playlist.getId().equals(playlistId)) {
                    isPlaylistDeleted = false;
                    break;
                }
            }
            Assert.assertTrue(isPlaylistDeleted);
        }
    }

    //*************************
    //*************************
    //       PUT TESTS
    //*************************
    //*************************

    @Test
    public void updatePlaylistDetails() {
        // CREATE PLAYLIST OBJECT
        // create request body
        Playlist playlist = Playlist.generatePlaylist();

        // CREATE PLAYLIST VIA API
        Playlist  originalPlaylist = RestfulPlaylist.createPlaylist(userId, playlist);

        // CREATE UPDATED PLAYLIST
        Playlist playlist1 = new Playlist();
        playlist1.setName("Updated Playlist " + System.currentTimeMillis());
        playlist1.setDescription("This bad boiiiii has been updated");
        playlist1.setPublic(true);

        // SEND PUT REQUEST
        RestfulPlaylist.updatePlaylistDetails(originalPlaylist.getId(), playlist1);

        // GET PLAYLIST
        Playlist resultingPlaylist = RestfulPlaylist.getPlaylist_byId(originalPlaylist.getId());

        // ASSERT
        // assert new & old data are different
        Assert.assertNotEquals(resultingPlaylist.getName(), originalPlaylist.getName());
        Assert.assertNotEquals(resultingPlaylist.getDescription(), originalPlaylist.getDescription());
        Assert.assertNotEquals(resultingPlaylist.getPublic(), originalPlaylist.getPublic());
        // assert the result has the updated values
        Assert.assertEquals(resultingPlaylist.getName(), playlist1.getName());
        Assert.assertEquals(resultingPlaylist.getDescription(), playlist1.getDescription());
        Assert.assertEquals(resultingPlaylist.getPublic(), playlist1.getPublic());
    }
}

