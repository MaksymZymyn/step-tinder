import database.Database;
import filters.AuthFilter;
import filters.CharsetFilter;
import servlets.UserServlet;
import servlets.LikeServlet;
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
        Server server = new Server(HerokuEnv.port());

        ServletContextHandler handler = new ServletContextHandler();
        var sfd = EnumSet.of(DispatcherType.REQUEST);

        handler.addServlet(new ServletHolder(new StaticFileServlet("static")), "/static/*");
        handler.addFilter(AuthFilter.class, "/users", sfd);
        handler.addFilter(CharsetFilter.class, "/users", sfd);
        handler.addServlet(new ServletHolder(new UserServlet()), "/users");
        handler.addFilter(AuthFilter.class, "/messages/*", sfd);
        handler.addFilter(CharsetFilter.class, "/messages/*", sfd);
        handler.addServlet(new ServletHolder(new MessagesServlet()), "/messages/*");
        handler.addFilter(AuthFilter.class, "/liked", sfd);
        handler.addFilter(CharsetFilter.class, "/liked", sfd);
        handler.addServlet(new ServletHolder(new LikeServlet()), "/liked");
        handler.addServlet(new ServletHolder(new LoginServlet()), "/login");
        handler.addServlet(new ServletHolder(new RegisterServlet()), "/register");
        handler.addServlet(new ServletHolder(new LogoutServlet()), "/logout");

        server.setHandler(handler);

        server.start();
        server.join();
    }
}
