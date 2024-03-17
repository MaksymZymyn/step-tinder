import database.Database;
import database.DatabaseSetup;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.LikeServlet;
import servlets.StaticFileServlet;
import utils.environment.HerokuEnv;

import java.sql.Connection;

public class App {
    public static void main(String[] args) throws Exception {
        DatabaseSetup.migrate(HerokuEnv.jdbc_url(), HerokuEnv.jdbc_username(), HerokuEnv.jdbc_password());

        Server server = new Server(HerokuEnv.port());

        ServletContextHandler handler = new ServletContextHandler();

        handler.addServlet(new ServletHolder(new StaticFileServlet("static")), "/static/*");
        handler.addServlet(new ServletHolder(new LikeServlet()), "/likes");

        server.setHandler(handler);

        server.start();
        server.join();
    }
}
