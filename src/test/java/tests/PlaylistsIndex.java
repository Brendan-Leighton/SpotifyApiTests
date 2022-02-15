package tests;
// TEST-NG

import models.TestData.Endpoint;
import org.testng.annotations.BeforeTest;
// REST-ASSURED
import io.restassured.response.Response;
// CUSTOM
import utils.Endpoints;
import utils.Excel;
import utils.Props;
import utils.restResources.RestResource;

import java.io.IOException;
import java.util.Map;


/**
 * Tests pertaining to the "playlist" endpoint for Spotify's API
 */
public class PlaylistsIndex {

    public static String userId;
    private static Excel excelData;

    @BeforeTest
    public void setup() {
        // GET USER's ID
        // send request
        Response userData = RestResource.get(Endpoints.ME);
        // extracting users' ID
        userId = userData.path("id");


        Map<String, Endpoint> endpoint = PlaylistsIndex.getExcel().mapSheet();
        System.out.println("\nIteration:\n" + endpoint + "\n");


    }

    /**
     * Get Test Data
     *
     * @return
     */
    public static Excel getExcel() {
        return Excel.getExcelSheet(0, Props.getTestDataPath());
    }
}

