import filters.AuthFilter;
import servlets.UserServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;
import servlets.*;
import utils.environment.HerokuEnv;
import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class App {
    public static void main(String[] args) throws Exception {
        // DatabaseSetup.migrate(HerokuEnv.jdbc_url(), HerokuEnv.jdbc_username(), HerokuEnv.jdbc_password());

        Server server = new Server(HerokuEnv.port());

        ServletContextHandler handler = new ServletContextHandler();
        var sfd = EnumSet.of(DispatcherType.REQUEST);

        handler.addServlet(new ServletHolder(new StaticFileServlet("static")), "/static/*");
        handler.addFilter(AuthFilter.class, "/liked", sfd);
        handler.addServlet(new ServletHolder(new LikeServlet()), "/liked");
        handler.addFilter(AuthFilter.class, "/messages/*", sfd);
        handler.addServlet(new ServletHolder(new MessagesServlet()), "/messages/*");
        handler.addFilter(AuthFilter.class, "/users", sfd);
        handler.addServlet(new ServletHolder(new UserServlet()), "/users");
        handler.addServlet(new ServletHolder(new LoginServlet()), "/login");

        server.setHandler(handler);

        server.start();
        server.join();
    }
}
