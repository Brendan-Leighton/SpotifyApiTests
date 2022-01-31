package utils;
// JAVA
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
// REST-ASSURED
import io.restassured.response.Response;
// MINE
import utils.restResources.RestResource;

/**
 * Manage Tokens for accessing users' Spotify profile information
 */
public class Tokens {

    private static String access_token;
    private static Instant token_lifespan;

    /**
     * Get access_token to access users' data. Refresh token if null or has expired
     * @return An access token as a String
     */
    public static String getAccessToken() {
        // if token is not set OR tokens' timer is up
        if (access_token == null || Instant.now().isAfter(token_lifespan)) {
            // refresh token
            Response res = refreshAccessToken();
            // extract token and its life expectancy
            access_token = res.path("access_token").toString();
            int expires_in = res.path("expires_in");
            token_lifespan = Instant.now().plusSeconds(expires_in);
        }
        return access_token;
    }

    /**
     * Uses a hardcoded refresh-token to refresh the access-token.
     * @return A response from a Post request that contains values such as an access_token and a lifespan for this new token.
     */
    public static Response refreshAccessToken() {
        // GET USERS' PERMISSION TO ACCESS THEIR SPOTIFY INFO
        // create requests' body
        Map<String, String> body_userToken = new HashMap<>();
        body_userToken.put("grant_type", Props.getGrantType());
        body_userToken.put("refresh_token", Props.getRefreshToken());
        body_userToken.put("client_id", Props.getClientId());
        body_userToken.put("client_secret", Props.getClientSecret());
        // get response
        return RestResource.post_Account(Endpoints.TOKEN, body_userToken);
    }
}
