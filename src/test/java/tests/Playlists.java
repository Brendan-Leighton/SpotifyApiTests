package tests;
// TEST-NG
import org.testng.Assert;
import org.testng.annotations.Test;
// REST-ASSURED
import io.restassured.response.Response;
// MINE
import utils.Endpoints;
import utils.RestResource;
import models.Playlist;

public class Playlists {

    @Test
    public void createPlaylist() {
        // GET USER DATA
        // send request
        Response userData = RestResource.get(Endpoints.ME);
        // extracting users' ID
        String userId = userData.path("id");

        // CREATE THE PLAYLIST
        // create request body
        Playlist playlist = new Playlist();
        playlist.setName("New Playlist " + System.currentTimeMillis());
        playlist.setDescription("Auto generated");
        playlist.setPublic(false);
        // send request
        Playlist res = RestResource.post(Endpoints.USERS + userId + Endpoints.PLAYLISTS, playlist).as(Playlist.class);

        // ASSERT
        Assert.assertEquals(res.getName(), playlist.getName());
    }
}
