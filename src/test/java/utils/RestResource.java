package utils;
// JAVA
import java.util.Map;
// REST-ASSURED
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
// MINE
import models.Playlist;

public class RestResource {

    /**
     * Specifies generic properties for a request to an endpoint of the API URI
     * @return RequestSpecification used for API endpoints
     */
    public static RequestSpecification reqSpec_Api() {
        return RestAssured.given().auth().oauth2(Tokens.getAccessToken()).baseUri(Props.getBaseURI_API()).log().all();
    }

    /**
     * Specifies generic properties for a request to an endpoint of the Account URI
     * @return RequestSpecification used for Account endpoints
     */
    public static RequestSpecification reqSpec_Account() {
        return RestAssured.given().baseUri(Props.getBaseURI_Accounts()).log().all();
    }

    /**
     * Specifies generic properties for a response from a request of the API or Account URIs
     * @return ResponseSpecification used in a ".spec" method
     */
    public static ResponseSpecification resSpec() {
        return new ResponseSpecBuilder().log(LogDetail.ALL).build();
    }

    /**
     * Simple GET request to an API URI
     * @param endpoint one of {@link Endpoints}
     * @return a Response from a get request
     */
    public static Response get(String endpoint) {
        return  reqSpec_Api().when().get(endpoint).then().spec(resSpec()).extract().response();
    }

    /**
     * Simple POST request to an API URI
     * @param endpoint one of {@link Endpoints}
     * @param body the requests' body. The thing you're trying to POST should match one of {@link models}. If creating a playlist, this is the {@link Playlist} object.
     * @return a Response from a post request with details of the newly posted thing.
     */
    public static Response post(String endpoint, Object body) {
        return  reqSpec_Api().body(body).when().post(endpoint).then().spec(resSpec()).extract().response();
    }

    /**
     * Simple POST request to an Account URI
     * @param endpoint one of {@link Endpoints}
     * @param body_userToken the requests' body. Contains properties such as
     *                   <ul>
     *                       <li>"grant_type"</li>
     *                       <li>"refresh_token"</li>
     *                       <li>"client_id"</li>
     *                       <li>"client_secret"</li>
     *                  </ul>
     * @return Response from a POST request, contains data such as "access_token".
     */
    public static Response post_Account(String endpoint, Map<String, String> body_userToken) {
        return  reqSpec_Account().formParams(body_userToken).when().post(endpoint).then().spec(resSpec()).extract().response();
    }
}
