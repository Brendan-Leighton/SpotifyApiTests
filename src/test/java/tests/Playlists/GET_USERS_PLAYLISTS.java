package tests.Playlists;
// JAVA
import java.util.List;
// TEST-NG
import org.testng.Assert;
import org.testng.annotations.Test;
// MINE
import tests.PlaylistsIndex;
import utils.restResources.RestfulPlaylist;
import models.Playlist;

public class GET_USERS_PLAYLISTS extends PlaylistsIndex {
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
        RestfulPlaylist.deleteAllPlaylists_forSingleUser(PlaylistsIndex.userId);

        // CREATE A PLAYLIST TO ADD
        Playlist playlistToAdd = Playlist.generatePlaylist();

        // ADD PLAYLIST
        RestfulPlaylist.createPlaylist(PlaylistsIndex.userId, playlistToAdd);

        // GET ALL PLAYLISTS FOR USER
        List<Playlist> resultingPlaylists = RestfulPlaylist.getAllPlaylists_forSingleUser(PlaylistsIndex.userId);

        // ASSERT
        // user has 1 playlist
        Assert.assertEquals(resultingPlaylists.size(), 1);
        // playlistId matches added playlist
        Assert.assertEquals(resultingPlaylists.get(0).getName(), playlistToAdd.getName());
    }
}
