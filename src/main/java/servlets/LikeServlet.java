package servlets;

import likes.*;
import lombok.SneakyThrows;
import users.*;
import auth.Auth;
import utils.exceptions.InvalidUserDataException;
import utils.FreemarkerService;

import javax.servlet.http.*;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class LikeServlet extends HttpServlet {

    private final UserService userService;
    private final LikeService likeService;

    public LikeServlet(UserService userService, LikeService likeService) {
        this.userService = userService;
        this.likeService = likeService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Auth.getCookieValue(req)
                .ifPresentOrElse(
                        user -> {
                            User currentUser;
                            try {
                                currentUser = Auth.getCurrentUser(userService, req);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            } catch (InvalidUserDataException e) {
                                throw new RuntimeException(e);
                            }

                            FreemarkerService freemarker;
                            try {
                                freemarker = new FreemarkerService("/path/to/templates");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            HashMap<String, Object> data = new HashMap<>();

                            Optional<Like> currentUserLike = likeService.get(currentUser.getId());
                            currentUserLike.ifPresentOrElse(
                                    like -> {
                                        UUID likedUserId = like.getUser_to();
                                        User likedUser;
                                        try {
                                            likedUser = userService.get(likedUserId);
                                        } catch (SQLException | InvalidUserDataException e) {
                                            throw new RuntimeException(e);
                                        }
                                        data.put("liked", Collections.singletonList(likedUser));
                                    },
                                    () -> data.put("liked", Collections.emptyList())
                            );

                            data.put("full_name", currentUser.getFullName());
                            try (PrintWriter w = resp.getWriter()) {
                                freemarker.render("people-list.html", data, w);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        () -> Auth.renderUnregistered(resp)
                );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Redirect to the doGet method
        resp.sendRedirect(req.getRequestURI());
    }
}
