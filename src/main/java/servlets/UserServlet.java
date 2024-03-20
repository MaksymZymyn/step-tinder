package servlets;

import auth.Auth;
import likes.*;
import lombok.Data;
import users.*;
import utils.FreemarkerService;
import utils.exceptions.UserNotFoundException;

import javax.servlet.http.*;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

@Data
public class UserServlet extends HttpServlet {
    UserService userService;
    LikeService likeService;
    FreemarkerService freemarker;

    public UserServlet() throws IOException {
        this.userService = new UserService(new UserDAO());
        this.likeService = new LikeService(new LikeDAO());
        this.freemarker = new FreemarkerService("templates");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Auth.getCookieValue(req)
                .ifPresentOrElse(
                        userUUID -> {
                            User currentUser;
                            try {
                                currentUser = userService.get(UUID.fromString(userUUID));
                            } catch (SQLException | UserNotFoundException e) {
                                throw new RuntimeException(e);
                            }

                            HashMap<String, Object> data = new HashMap<>();

                            data.put("user_name", currentUser.getUsername());
                            data.put("full_name", currentUser.getFullName());
                            data.put("picture", currentUser.getPicture());
                            try (PrintWriter w = resp.getWriter()) {
                                freemarker.render("people-list.ftl", data, w);
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Auth.getCookieValue(req)
                .ifPresentOrElse(
                        user -> {
                            String username = req.getParameter("username");
                            String fullName = req.getParameter("fullName");
                            String picture = req.getParameter("picture");
                            String password = req.getParameter("password");
                            try {
                                userService.insert(username, fullName, picture, password);
                            } catch (SQLException e) {
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
}
