package servlets;

import auth.Auth;
import lombok.*;
import users.*;
import utils.FreemarkerService;
import utils.exceptions.UserNotFoundException;

import javax.servlet.http.*;
import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;

@Data
public class LoginServlet extends HttpServlet {

    UserService userService;
    FreemarkerService freemarker;

    public LoginServlet() throws IOException {
        this.userService = new UserService(new UserDAO());
        this.freemarker = new FreemarkerService("templates");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Auth.getCookieValue(req).ifPresentOrElse(
                cookieValue -> {
                    try {
                        resp.sendRedirect("/liked");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> {
                    try {
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("error", "");
                        freemarker.render("login.ftl", data, resp.getWriter());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("login");
        String password = req.getParameter("password");

        try {
            User user = userService.get(username);

            if (user.checkPassword(password)) {
                Auth.setCookieValue(user.getId().toString(), resp);
                resp.sendRedirect("/liked");
            } else {
                HashMap<String, Object> data = new HashMap<>();
                data.put("error", "Invalid username or password.");
                freemarker.render("login.ftl", data, resp.getWriter());
            }
        } catch (SQLException | UserNotFoundException e) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("error", "Invalid username or password.");
            freemarker.render("login.ftl", data, resp.getWriter());
        }
    }
}
