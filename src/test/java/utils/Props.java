package utils;
// JAVA
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Access props from .properties files
 */
public class Props {

    private static Properties properties;

    private Props() {
    }

    /**
     * Gets, and possibly initially loads, properties from ".properties" files
     * @return an object containing getters for all properties.
     */
    public static Properties init() {

        if (properties == null) {

            properties = new Properties();
            try {
                properties.load(new FileInputStream("src/test/resources/hidden/API_config.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Properties properties2 = new Properties();
            try {
                properties2.load(new FileInputStream("src/test/resources/URI.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            properties.putAll(properties2);
        }
        return properties;
    }

    /**
     * hidden property
     * @return essentially a username needed to access Spotify's API
     */
    public static String getClientId() {
        return init().getProperty("client_id");
    }

    /**
     * hidden property
     * @return essentially a password needed to access Spotify's API
     */
    public static String getClientSecret() {
        return init().getProperty("client_secret");
    }

    /**
     * hidden property
     * @return a token that lets us get a new access_token when our old one expires
     */
    public static String getRefreshToken() {
        return init().getProperty("refresh_token");
    }

    /**
     * hidden property
     * @return Not sure, could be what type of token we're looking to get?
     */
    public static String getGrantType() {
        return init().getProperty("grant_type");
    }

    /**
     *
     * @return the beginning of the url you'll want to hit. You'll need to append an {@link Endpoints}
     */
    public static String getBaseURI_Accounts() {
        return init().getProperty("baseURI_accounts");
    }

    /**
     *
     * @return the beginning of the url you'll want to hit. You'll need to append an {@link Endpoints}
     */
    public static String getBaseURI_API() {
        return init().getProperty("baseURI_api");
    }
}
