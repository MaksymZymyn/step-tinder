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
                        currentUser -> {
                            String userIdParam = req.getParameter("id");
                            try {
                                UUID userId = UUID.fromString(userIdParam);

                                User user = userService.get(userId);

                                boolean hasLiked = likeService.hasBeenLiked(userId);

                                HashMap<String, Object> data = new HashMap<>();
                                data.put("user", user);
                                data.put("hasLiked", hasLiked);

                                freemarker.render("like-page.ftl", data, resp.getWriter());
                            } catch (SQLException | IOException | UserNotFoundException e) {
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
                                        likeService.insert(new Like(UUID.randomUUID(), getCurrentUserId(req), userId, true));
                                        break;
                                    case "dislike":
                                        likeService.insert(new Like(UUID.randomUUID(), getCurrentUserId(req), userId, false));
                                        break;
                                    default:
                                        resp.getWriter().write("Error: Invalid action parameter");
                                        return;
                                }

                                resp.sendRedirect("/liked?id=" + userIdParam);
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

    private UUID getCurrentUserId(HttpServletRequest req) {
        return UUID.fromString(Auth.getCookieValueForced(req));
    }
}
