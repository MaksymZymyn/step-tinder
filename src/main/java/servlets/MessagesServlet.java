package servlets;

import auth.Auth;
import messages.Message;
import messages.MessageDAO;
import messages.MessageService;
import users.User;
import users.UserDAO;
import users.UserService;
import utils.FreemarkerService;
import utils.exceptions.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MessagesServlet extends HttpServlet {
    MessageService messageService;
    private final UserService userService;
    private final FreemarkerService freemarker;

    public MessagesServlet() throws IOException {
        this.messageService = new MessageService(new MessageDAO());
        this.userService = new UserService(new UserDAO());
        this.freemarker = new FreemarkerService("templates");
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        UUID idFrom = UUID.fromString(Auth.getCookieValueForced(req));
        String idToParam;
        try {
            idToParam = req.getPathInfo().split("/")[1];
        } catch (IndexOutOfBoundsException e) {
            resp.setStatus(400);
            return;
        }
        UUID idTo = UUID.fromString(idToParam);
        User chatWithUser;
        try {
            chatWithUser = userService.get(idTo);
        } catch (SQLException | UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<Message> messages = messageService.ReadMessageFromDialog(idFrom, idTo);
        HashMap<String, Object> templateData = new HashMap<>();
        templateData.put("chatWithUser", chatWithUser);
        templateData.put("messages", messages);
        try (PrintWriter w = resp.getWriter()) {
            freemarker.render("chat.ftl", templateData, w);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        UUID idFrom = UUID.fromString(Auth.getCookieValueForced(req));
        String idToParam;
        try {
            idToParam = req.getPathInfo().split("/")[1];
        } catch (IndexOutOfBoundsException e) {
            resp.setStatus(400);
            return;
        }
        UUID idTo = UUID.fromString(idToParam);

        String messageText = req.getParameter("messageText");
        Message message = new Message(idFrom, idTo, messageText);
        System.out.println(resp);
        try {
            if (messageService.addMessage(message)) {
                try {
                    System.out.println(idFrom);
                    System.out.println(idTo);
                    updateChat(resp, idFrom, idTo); // Передаем idFrom и idTo
                } catch (SQLException | UserNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateChat(HttpServletResponse resp, UUID idFrom, UUID idTo) throws SQLException, UserNotFoundException {
        List<Message> messages = messageService.ReadMessageFromDialog(idFrom, idTo);
        User chatWithUser = userService.get(idTo);
        HashMap<String, Object> templateData = new HashMap<>();
        templateData.put("chatWithUser", chatWithUser);
        templateData.put("messages", messages);

        try (PrintWriter w = resp.getWriter()) {
            freemarker.render("chat.ftl", templateData, w);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
