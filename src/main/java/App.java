import database.Database;
import filters.AuthFilter;
import servlets.UserServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;
import servlets.*;
import utils.environment.HerokuEnv;
import javax.servlet.DispatcherType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.EnumSet;

public class App {
    public static void main(String[] args) throws Exception {
        // DatabaseSetup.migrate(HerokuEnv.jdbc_url(), HerokuEnv.jdbc_username(), HerokuEnv.jdbc_password());

        Server server = new Server(HerokuEnv.port());

        ServletContextHandler handler = new ServletContextHandler();

        handler.addServlet(new ServletHolder(new StaticFileServlet("static")), "/static/*");
        handler.addServlet(new ServletHolder(new LikeServlet()), "/users");
        handler.addFilter(AuthFilter.class, "/users", EnumSet.of(DispatcherType.REQUEST));
        handler.addServlet(new ServletHolder(new MessagesServlet()), "/messages/*");
        handler.addFilter(AuthFilter.class, "/messages/*", EnumSet.of(DispatcherType.REQUEST));
        handler.addServlet(new ServletHolder(new UserServlet()), "/liked");
        handler.addFilter(AuthFilter.class, "/liked", EnumSet.of(DispatcherType.REQUEST));
        handler.addServlet(new ServletHolder(new LoginServlet()), "/login");
        handler.addServlet(new ServletHolder(new RegisterServlet()), "/register");

        server.setHandler(handler);

        server.start();
        server.join();
    }
}
