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
    private UserService userService;
    private LikeService likeService;
    private FreemarkerService freemarker;

    public UserServlet() throws IOException {
        this.userService = new UserService(new UserDAO());
        this.likeService = new LikeService(new LikeDAO());
        this.freemarker = new FreemarkerService("templates");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Auth.getCookieValue(req)
                .ifPresentOrElse(
                        user -> {
                            HashMap<String, Object> data = new HashMap<>();

                            int counter = 0;

                            List<Like> unliked;
                            try {
                                unliked = likeService.getByChoice(false);
                                for (Like like : unliked) {
                                    User unlikedUser = userService.get(like.getUser_to());
                                    data.put("id", unlikedUser.getId());
                                    data.put("username", unlikedUser.getUsername());
                                    data.put("fullName", unlikedUser.getFullName());
                                    data.put("picture", unlikedUser.getPicture());

                                    try (PrintWriter w = resp.getWriter()) {
                                        freemarker.render("like-page.ftl", data, w);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    counter++;
                                }
                            } catch (SQLException | UserNotFoundException e) {
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
        String likedParam = req.getParameter("liked");
        Auth.getCookieValue(req)
                .ifPresentOrElse(
                        user -> {
                            if (likedParam != null) {
                                List<Like> liked;
                                try {
                                    liked = likeService.getByChoice(true);
                                    for (Like like : liked) {
                                        User likedUser = userService.get(like.getUser_to());
                                        if (!likeService.hasBeenLiked(likedUser.getId())) {
                                            likeService.insert(like);
                                        }
                                    }
                                    if (likeService.getByChoice(false).isEmpty()) {
                                        resp.sendRedirect("/liked");
                                    }
                                } catch (SQLException | UserNotFoundException | IOException e) {
                                    throw new RuntimeException(e);
                                }
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
