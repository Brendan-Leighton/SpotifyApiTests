package tests.Playlists;

import models.Playlist;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.PlaylistsIndex;
import utils.restResources.RestfulPlaylist;

public class CREATE_PLAYLIST extends PlaylistsIndex {
    /*
        TEST : CREATE PLAYLIST

        Requirements
            1. QUERY: userId - String
            2. BODY: name - String
        Optional
            1. BODY: public - boolean
            2. BODY: collaborative - boolean
            3. BODY: description - String

        Tests
            Complete:
            Incomplete:
                Create_Playlist_01 - Payload has a valid name and user_id
                Create_Playlist_02 - Provide an invalid user_id
                Create_Playlist_03 - Provide an empty user_id
                Create_Playlist_04 - Payload name is an empty String
                Create_Playlist_05 - public as valid Boolean: true
                Create_Playlist_06 - public as valid Boolean: false
                Create_Playlist_07 - public as non-Boolean data type
                Create_Playlist_08 - public as blank ""
                Create_Playlist_09 - collaborative as valid Boolean: true
                Create_Playlist_10 - collaborative as valid Boolean: false
                Create_Playlist_11 - collaborative as non-Boolean data type
                Create_Playlist_12 - collaborative as blank ""
                Create_Playlist_13 - description as valid String
                Create_Playlist_14 - description as non-String data type
                Create_Playlist_15 - description as blank ""
     */

    @Test
    private static void createPlaylist() {
        // CREATE THE PLAYLIST
        // create request body
        Playlist playlist = new Playlist();
        playlist.setName("New Playlist " + System.currentTimeMillis());
        playlist.setDescription("Auto generated");
        playlist.setPublic(false);
        // send request
        Playlist res = RestfulPlaylist.createPlaylist(PlaylistsIndex.userId, playlist);

        // ASSERT
        Assert.assertEquals(res.getName(), playlist.getName());
    }
}
