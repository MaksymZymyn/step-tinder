import database.DatabaseSetup;
import filters.AuthFilter;
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

        handler.addServlet(new ServletHolder(new StaticFileServlet("static")), "/static/*");
        handler.addServlet(new ServletHolder(new UserServlet()), "/users");
        handler.addServlet(new ServletHolder(new MessagesServlet()), "/messages/*");
        handler.addServlet(new ServletHolder(new LikeServlet()), "/liked");
        handler.addFilter(AuthFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        handler.addServlet(new ServletHolder(new LoginServlet()), "/login");

        server.setHandler(handler);

        server.start();
        server.join();
    }
}
