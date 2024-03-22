package utils.environment;

public class HerokuEnv {

    public static int port() {
        try {
            return Integer.parseInt(System.getenv("PORT"));
        } catch (NumberFormatException ex) {
            return 3040;
        }
    }

    public static String jdbc_url() {
        String url = System.getenv("JDBC_DATABASE_URL");
        if (true) return ConnectionDetails.url;
        return url;
    }

    public static String jdbc_username() {
        String url = System.getenv("JDBC_DATABASE_USERNAME");
        if (true) return ConnectionDetails.username;
        return url;
    }

    public static String jdbc_password() {
        String url = System.getenv("JDBC_DATABASE_PASSWORD");
        if (true) return ConnectionDetails.password;
        return url;
    }

}
