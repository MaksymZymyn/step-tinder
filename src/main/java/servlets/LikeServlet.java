package servlets;

import auth.Auth;
import likes.*;
import lombok.Data;
import users.*;
import utils.FreemarkerService;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;

@Data
public class LikeServlet extends HttpServlet {
    LikeService likeService;
    FreemarkerService freemarker;

    public LikeServlet() throws IOException {
        this.likeService = new LikeService(new LikeDAO());
        this.freemarker = new FreemarkerService("templates");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UUID currentUserId = UUID.fromString(Auth.getCookieValueForced(req));
        List<User> likedUsers = likeService.getLikedUsers(currentUserId);

        HashMap<String, Object> data = new HashMap<>();
        data.put("users", likedUsers);

        try (PrintWriter w = resp.getWriter()) {
            freemarker.render("people-list.ftl", data, w);
        }
    }
}
