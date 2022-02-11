package tests.Users;

import models.Playlist;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.PlaylistsIndex;
import utils.restResources.RestfulPlaylist;

import java.util.List;

public class UNFOLLOW_PLAYLIST extends PlaylistsIndex {
    /**
     * DELETE a playlist by its ID.
     *
     * Spotify doesn't actually delete playlists just makes the user unfollow it. So, by "delete" we always mean unfollow. It will no longer show up the in the users' list of playlists
     */
    @Test
    public void deletePlaylistById() {

        // GET PLAYLISTS
        List<Playlist> res_usersPlaylists_beforeDel = RestfulPlaylist.getAllPlaylists_forSingleUser(PlaylistsIndex.userId);

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
            res_usersPlaylists_beforeDel = RestfulPlaylist.getAllPlaylists_forSingleUser(PlaylistsIndex.userId);
        }

        // GET ID OF A PLAYLIST
        String playlistId = res_usersPlaylists_beforeDel.get(0).getId();

        // DELETE PLAYLIST
        RestfulPlaylist.deletePlaylist_byId(playlistId);

        // GET PLAYLISTS
        List<Playlist> res_usersPlaylists_afterDel = RestfulPlaylist.getAllPlaylists_forSingleUser(PlaylistsIndex.userId);

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

}
