import database.Database;
import database.DatabaseSetup;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.HelloServlet;
import utils.environment.ConnectionDetails;
import utils.environment.HerokuEnv;
import java.sql.Connection;

public class App {

    public static void main(String[] args) throws Exception {
        // temporary
        DatabaseSetup.migrate(ConnectionDetails.getUrl(), ConnectionDetails.getUsername(), ConnectionDetails.getPassword());
        // final
        // DbSetup.migrate(HerokuEnv.jdbc_url(), HerokuEnv.jdbc_username(), HerokuEnv.jdbc_password());

        // temporary
        Connection conn = Database.create(ConnectionDetails.getUrl(), ConnectionDetails.getUsername(), ConnectionDetails.getPassword());
        // final
        // Connection conn = Database.createFromURL(HerokuEnv.jdbc_url());
        // Connection conn = null;

        Server server = new Server(HerokuEnv.port());

        ServletContextHandler handler = new ServletContextHandler();
        handler.addServlet(HelloServlet.class, "/hello");
        // handler.addServlet(new ServletHolder(new UsersServlet(conn)), "/users");
        server.setHandler(handler);

        server.start();
        server.join();
    }
}
