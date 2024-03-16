import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.StaticFileServlet;
import users.UserDAO;
import utils.environment.HerokuEnv;

public class App {
    public static void main(String[] args) throws Exception {
        Server server = new Server(HerokuEnv.port());

        ServletContextHandler handler = new ServletContextHandler();

        handler.addServlet(new ServletHolder(new StaticFileServlet("static")), "/static/*");

        UserDAO dao = new UserDAO();
        dao.insert("yalukaiwo", "Ponomarenko Luka", "asd", "password");

        server.setHandler(handler);

        server.start();
        server.join();
    }
}
