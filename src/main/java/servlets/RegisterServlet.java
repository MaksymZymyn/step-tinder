package servlets;

import auth.Auth;
import users.User;
import users.UserDAO;
import users.UserService;
import utils.FreemarkerService;
import utils.exceptions.RegistrationException;
import utils.exceptions.UserNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class RegisterServlet extends HttpServlet {
    UserService userService;
    FreemarkerService freemarker;

    public RegisterServlet() throws IOException {
        this.userService = new UserService(new UserDAO());
        this.freemarker = new FreemarkerService("templates");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Auth.getCookieValue(req).ifPresentOrElse(
                cookieValue -> {
                    try {
                        resp.sendRedirect("/users");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> {
                    try {
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("error", "");
                        freemarker.render("register.ftl", data, resp.getWriter());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("login");
        String fullName = req.getParameter("name");
        String picture = req.getParameter("picture");
        String password = req.getParameter("password");

        System.out.println(username);
        System.out.println(fullName);
        System.out.println(picture);
        System.out.println(password);

        try {
            User user = userService.insert(username, fullName, picture, password);

            Auth.setCookieValue(user.getId().toString(), resp);
            resp.sendRedirect("/users");
        } catch (SQLException | RegistrationException e) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("error", "Registration failed. User with such username might already exist");
            freemarker.render("register.ftl", data, resp.getWriter());
        }
    }
}
