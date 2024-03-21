package servlets;

import lombok.*;
import users.UserDAO;
import users.UserService;
import utils.FreemarkerService;
import auth.Auth;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Data
public class LogoutServlet extends HttpServlet {
    FreemarkerService freemarker;

    public LogoutServlet() throws IOException {
        this.freemarker = new FreemarkerService("templates");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Auth.clearCookie(resp);
        resp.sendRedirect("/login");
    }
}
