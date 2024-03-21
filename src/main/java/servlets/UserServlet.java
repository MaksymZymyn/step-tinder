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
    User nextUser;

    public UserServlet() throws IOException {
        this.userService = new UserService(new UserDAO());
        this.likeService = new LikeService(new LikeDAO());
        this.freemarker = new FreemarkerService("templates");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            UUID currentUserId = UUID.fromString(Auth.getCookieValueForced(req));
            Optional<User> nextUserOptional = userService.nextUser(currentUserId);
            if (nextUserOptional.isPresent()) {
                nextUser = nextUserOptional.get();
                HashMap<String, Object> data = new HashMap<>();
                data.put("picture", nextUser.getPicture());
                data.put("fullName", nextUser.getFullName());
                data.put("username", nextUser.getUsername());
                data.put("id", nextUser.getId());

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
        UUID currentUserId = UUID.fromString(Auth.getCookieValueForced(req));
        if (nextUser != null) {
            try {
                String action = req.getParameter("action");

                if (action == null || action.isEmpty()) {
                    resp.getWriter().write("Error: Missing action parameter");
                    return;
                }

                switch (action) {
                    case "like":
                        doGet(req, resp);
                        likeService.insert(new Like(UUID.randomUUID(), currentUserId, nextUser.getId(), true));
                        break;
                    case "dislike":
                        doGet(req, resp);
                        likeService.insert(new Like(UUID.randomUUID(), currentUserId, nextUser.getId(), false));
                        break;
                    default:
                        resp.getWriter().write("Error: Invalid action parameter");
                        return;
                }


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            resp.sendRedirect("/liked");
        }
    }
}
