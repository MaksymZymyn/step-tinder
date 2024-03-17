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
        Auth.getCookieValue(req)
                .ifPresentOrElse(
                        user -> {
                            HashMap<String, Object> data = new HashMap<>();
                            try (PrintWriter w = resp.getWriter()) {
                                freemarker.render("login.ftl", data, w);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        () -> {
                            try {
                                Auth.renderUnregistered(resp);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String logout = req.getParameter("logout");
        String username = req.getParameter("username");

        if (logout != null) {
            Auth.clearCookie(resp);
            resp.sendRedirect("/login");
        } else {
            try {
                if (userService.get(username) != null) {
                    Cookie cookie = new Cookie("id",
                            userService.get(username).getId().toString());
                    resp.addCookie(cookie);
                    resp.sendRedirect("/users");
                } else {
                    resp.sendRedirect("/login");
                }
            } catch (SQLException | UserNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
