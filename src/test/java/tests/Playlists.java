package tests;
// TEST-NG
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
// REST-ASSURED
import io.restassured.response.Response;
// MINE
import utils.Endpoints;
import utils.RestResource;
import models.Playlist;

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
        Playlist res = RestResource.post(Endpoints.USERS + userId + Endpoints.PLAYLISTS, playlist).as(Playlist.class);

        // ASSERT
        Assert.assertEquals(res.getName(), playlist.getName());
    }

    //*************************
    //*************************
    //       GET TESTS
    //*************************
    //*************************

    @Test
    public void getUsersPlaylists() {

    }
}
