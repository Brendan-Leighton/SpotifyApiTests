package tests.Playlists;

import models.Playlist;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.PlaylistsIndex;
import utils.restResources.RestfulPlaylist;

public class CHANGE_PLAYLIST_DETAILS extends PlaylistsIndex {
    @Test
    public void updatePlaylistDetails() {
        // CREATE PLAYLIST OBJECT
        // create request body
        Playlist playlist = Playlist.generatePlaylist();

        // CREATE PLAYLIST VIA API
        Playlist  originalPlaylist = RestfulPlaylist.createPlaylist(PlaylistsIndex.userId, playlist);

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
