package tests.Playlists;
// JAVA
import java.util.List;
// TEST-NG
import org.testng.Assert;
import org.testng.annotations.Test;
// MINE
import utils.restResources.RestfulPlaylist;
import models.Playlist;

public class GET_USERS_PLAYLISTS extends Playlists{
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
}
