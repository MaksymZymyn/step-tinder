package servlets;

import likes.*;
import lombok.*;
import users.*;
import auth.Auth;
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
                        user -> {
                            User currentUser;
                            try {
                                currentUser = Auth.getCurrentUser(userService, req).orElseThrow(RuntimeException::new);
                            } catch (SQLException e) {
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
                                        } catch (SQLException | UserNotFoundException e) {
                                            throw new RuntimeException(e);
                                        }
                                        data.put("liked", Collections.singletonList(likedUser));
                                    },
                                    () -> data.put("liked", Collections.emptyList())
                            );

                            data.put("full_name", currentUser.getFullName());
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HashMap<String, Object> data = new HashMap<>();
        try (PrintWriter w = resp.getWriter()) {
            freemarker.render("people-list.ftl", data, w);
        }
    }
}
