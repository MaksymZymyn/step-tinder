package utils.environment;

public class ConnectionDetails {

    public static String getUrl() {
        return System.getenv("jdbc_url");
    }

    public static String getUsername() {
        return System.getenv("jdbc_username");
    }

    public static String getPassword() {
        return System.getenv("jdbc_password");
    }
}
