package tests.Playlists;

import models.Playlist;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.restResources.RestfulPlaylist;

public class CreatePlaylist extends Playlists{
    /*
        TEST : CREATE PLAYLIST

        Requirements
            1. userId
            2. name - String
        Optional
            1. public - boolean
            2. collaborative - boolean
            3. description - String

        Tests
            1. include name
            2. exclude name
            3. include name of empty String ""
            4. no userId
            5. invalid userId

     */
    @Test
    public void createPlaylist() {

        // CREATE THE PLAYLIST
        // create request body
        Playlist playlist = new Playlist();
        playlist.setName("New Playlist " + System.currentTimeMillis());
        playlist.setDescription("Auto generated");
        playlist.setPublic(false);
        // send request
        Playlist res = RestfulPlaylist.createPlaylist(this.userId, playlist);

        // ASSERT
        Assert.assertEquals(res.getName(), playlist.getName());
    }
}
