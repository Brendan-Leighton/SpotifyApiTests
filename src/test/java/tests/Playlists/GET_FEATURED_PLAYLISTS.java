package tests.Playlists;

import models.Playlist;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.PlaylistsIndex;
import utils.restResources.RestfulPlaylist;

import java.util.List;

public class GET_FEATURED_PLAYLISTS extends PlaylistsIndex {

    @Test
    public void getFeaturedPlaylists() {
        List<Playlist> playlists = RestfulPlaylist.getAllPlaylists_featured();
        Assert.assertTrue(playlists.size() > 0);
    }
}
