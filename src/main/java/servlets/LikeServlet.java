package servlets;

import auth.Auth;
import likes.*;
import lombok.Data;
import users.*;
import utils.FreemarkerService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

@Data
public class LikeServlet extends HttpServlet {
    UserService userService;
    LikeService likeService;
    FreemarkerService freemarker;

    public LikeServlet() throws IOException {
        this.userService = new UserService(new UserDAO());
        this.likeService = new LikeService(new LikeDAO());
        this.freemarker = new FreemarkerService("templates");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Auth.getCookieValue(req)
                .ifPresentOrElse(
                        cookieValue -> {
                            try {
                                List<User> users = userService.getAll();
                                HashMap<String, Object> data = new HashMap<>();
                                for (int i = 0; i < users.size(); i++) {
                                    data.put("picture", users.get(i).getPicture());
                                    data.put("fullName", users.get(i).getFullName());
                                    data.put("username", users.get(i).getUsername());
                                    data.put("id", users.get(i).getId());
                                }

                                freemarker.render("like-page.ftl", data, resp.getWriter());
                            } catch (IOException | SQLException e) {
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
                        currentUser -> {
                            String userIdParam = req.getParameter("id");
                            try {
                                UUID userId = UUID.fromString(userIdParam);
                                String action = req.getParameter("action");

                                if (action == null || action.isEmpty()) {
                                    resp.getWriter().write("Error: Missing action parameter");
                                    return;
                                }

                                switch (action) {
                                    case "like":
                                        likeService.insert(new Like(UUID.randomUUID(), UUID.fromString(currentUser), userId, true));
                                        break;
                                    case "dislike":
                                        likeService.insert(new Like(UUID.randomUUID(), UUID.fromString(currentUser), userId, false));
                                        break;
                                    default:
                                        resp.getWriter().write("Error: Invalid action parameter");
                                        return;
                                }

                                List<User> users = userService.getAll();
                                int currentIndex = -1;
                                for (int i = 0; i < users.size(); i++) {
                                    if (users.get(i).getId().equals(userId)) {
                                        currentIndex = i;
                                        break;
                                    }
                                }

                                if (currentIndex != -1 && currentIndex < users.size() - 1) {
                                    User nextUser = users.get(currentIndex + 1);
                                    req.setAttribute("user", nextUser);
                                    req.getRequestDispatcher("/WEB-INF/user-profile.jsp").forward(req, resp);
                                } else {
                                    resp.getWriter().write("No more users to display.");
                                }
                            } catch (IOException | SQLException | ServletException e) {
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
