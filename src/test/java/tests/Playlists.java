package tests;
// JAVA
import java.util.HashMap;
import java.util.Map;
// TEST-NG
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
// REST-ASSURED
import io.restassured.response.Response;
import io.restassured.RestAssured;
// MINE
import utils.Endpoints;
import utils.Props;
import utils.Tokens;

public class Playlist {

    @Test
    public void createPlaylist() {

        String access_token = Tokens.getAccessToken();

        // GET USERS' SPOTIFY INFO
        // sending access_token
        Response userData = RestAssured.given().auth().oauth2(access_token).log().all()
                .when().get(Props.getBaseURI_API() + Endpoints.ME)
                .then().log().all().extract().response();
        // extracting users' ID
        String userId = userData.path("id");

        // CREATE THE PLAYLIST
        // uses access_token and userId from above code
        RestAssured.given().auth().oauth2(access_token)
                .body("{\n" +
                "  \"name\": \"New Playlist\",\n" +
                "  \"description\": \"New playlist description\",\n" +
                "  \"public\": false\n" +
                "}")
                .log().all()
                .when().post(Props.getBaseURI_API() + Endpoints.USERS + userId + Endpoints.PLAYLISTS)
                .then().log().all().assertThat().statusCode(201)
                .extract().response();
    }
}
