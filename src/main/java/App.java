import database.Database;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.HelloWorldServlet;
import utils.environment.HerokuEnv;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;

public class App {
    public static void main(String[] args) throws Exception {
        Server server = new Server(HerokuEnv.port());

        Connection conn = Database.connect(HerokuEnv.jdbc_url(), HerokuEnv.jdbc_username(), HerokuEnv.jdbc_password());

        ServletContextHandler handler = new ServletContextHandler();

        HttpServlet helloWorldServlet = new HelloWorldServlet();

        handler.addServlet(new ServletHolder(helloWorldServlet), "/hello");

        server.setHandler(handler);

        server.start();
        server.join();
    }
}
