package servlets;

import auth.Auth;
import likes.*;
import lombok.Data;
import users.*;
import utils.FreemarkerService;

import javax.servlet.http.*;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

@Data
public class UserServlet extends HttpServlet {
    UserService userService;
    LikeService likeService;
    FreemarkerService freemarker;
    private List<User> users;
    private int currentIndex = 0;

    public UserServlet() throws IOException {
        this.userService = new UserService(new UserDAO());
        this.likeService = new LikeService(new LikeDAO());
        this.freemarker = new FreemarkerService("templates");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            UUID currentUserId = UUID.fromString(Auth.getCookieValueForced(req));
            users = userService.getAllExcept(currentUserId);

            User targetUser = users.get(currentIndex);

            if (targetUser != null) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("picture", targetUser.getPicture());
                data.put("fullName", targetUser.getFullName());
                data.put("username", targetUser.getUsername());
                data.put("id", targetUser.getId());

                freemarker.render("like-page.ftl", data, resp.getWriter());
            } else {
                resp.getWriter().write("No users found.");
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            UUID currentUserId = UUID.fromString(Auth.getCookieValueForced(req));

            users = userService.getAllExcept(currentUserId);
            User targetUser = users.get(currentIndex);

            String choiceOfUser = req.getParameter("choice");

            if (choiceOfUser != null) {
                switch (choiceOfUser) {
                    case "Like":
                        likeService.insert(new Like(UUID.randomUUID(), currentUserId, targetUser.getId(), true));
                        currentIndex++;
                        break;
                    case "Dislike":
                        likeService.insert(new Like(UUID.randomUUID(), currentUserId, targetUser.getId(), false));
                        currentIndex++;
                        break;
                    default:
                        resp.getWriter().write("Error: Invalid action parameter");
                        return;
                }
            }

            if (currentIndex >= users.size()) {
                resp.sendRedirect("/liked");
                return;
            }
            resp.sendRedirect("/users");
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
