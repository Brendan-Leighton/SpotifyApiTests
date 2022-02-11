package tests.Playlists;
// TEST-NG
import org.testng.Assert;
import org.testng.annotations.Test;
// MINE
import models.Playlist;
import tests.PlaylistsIndex;
import utils.restResources.RestfulPlaylist;

public class GET_PLAYLIST extends PlaylistsIndex {
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

}
