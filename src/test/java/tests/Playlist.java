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

public class Playlist {

    @Test
    public void createPlaylist() {

        // GET USERS' PERMISSION TO ACCESS THEIR SPOTIFY INFO
        // create requests' body
        Map<String, String> body_userToken = new HashMap<>();
        body_userToken.put("grant_type", Props.getGrantType());
        body_userToken.put("refresh_token", Props.getRefreshToken());
        body_userToken.put("client_id", Props.getClientId());
        body_userToken.put("client_secret", Props.getClientSecret());
        // get response
        Response res = RestAssured.given().formParams(body_userToken).log().all()
                .when().post(Props.getBaseURI_Accounts() + Endpoints.TOKEN)
                .then().log().all().extract().response();
        // extract the token that proves we have users' permission
        String access_token = res.path("access_token").toString();

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
