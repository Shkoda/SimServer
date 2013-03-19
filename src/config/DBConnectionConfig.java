package config;

/**
 * Created with IntelliJ IDEA.
 * User: Nightingale
 * Date: 1/26/13
 * Time: 3:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBConnectionConfig {
    private static final String dbURL = "jdbc:postgresql://localhost/TestDB";
    private static final String dbUser = "testuser";
    private static final String dbPassword = "123";

    public static String getDbPassword() {
        return dbPassword;
    }

    public static String getDbUser() {
        return dbUser;
    }

    public static String getDbURL() {
        return dbURL;
    }
}
