package tests.Playlists;
// TEST-NG
import org.testng.annotations.BeforeTest;
// REST-ASSURED
import io.restassured.response.Response;
// MINE
import utils.Endpoints;
import utils.restResources.RestResource;


/**
 * Tests pertaining to the "playlist" endpoint for Spotify's API
 */
public class Playlists {

    static String userId;

    @BeforeTest
    public static void setup() {
        // GET USER's ID
        // send request
        Response userData = RestResource.get(Endpoints.ME);
        // extracting users' ID
        userId = userData.path("id");
    }
}

