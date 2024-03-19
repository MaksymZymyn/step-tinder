package servlets;

import auth.Auth;
import likes.*;
import lombok.*;
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
    private static int counter;
    public static ArrayList<User> users = new ArrayList<>();

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

                            counter = 0;

                            List<Like> unliked = likeService.getByChoice(false);
                            unliked.forEach(like -> {
                                try {
                                    User unlikedUser = userService.get(like.getUser_to());
                                    users.add(unlikedUser);
                                } catch (SQLException | UserNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                            data.put("id", users.get(counter).getId());
                            data.put("username", users.get(counter).getUsername());
                            data.put("fullName", users.get(counter).getFullName());
                            data.put("picture", users.get(counter).getPicture());

                            try (PrintWriter w = resp.getWriter()) {
                                freemarker.render("like-page.ftl", data, w);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            counter++;
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
                                List<Like> liked = likeService.getByChoice(true);
                                for (Like like : liked) {
                                    try {
                                        User likedUser = userService.get(like.getUser_to());
                                        users.add(likedUser);

                                        if (!likeService.hasBeenLiked(likedUser.getId())) {
                                            likeService.insert(like);
                                        }
                                    } catch (SQLException | UserNotFoundException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                if (counter == likeService.getByChoice(false).size()) {
                                    try {
                                        resp.sendRedirect("/liked");
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    counter = 0;
                                }
                                HashMap<String, Object> data = new HashMap<>();

                                counter = 0;

                                data.put("id", users.get(counter).getId());
                                data.put("username", users.get(counter).getUsername());
                                data.put("fullName", users.get(counter).getFullName());
                                data.put("picture", users.get(counter).getPicture());

                                try (PrintWriter w = resp.getWriter()) {
                                    freemarker.render("like-page.ftl", data, w);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                counter++;
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
