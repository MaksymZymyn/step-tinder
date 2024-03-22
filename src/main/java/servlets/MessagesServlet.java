package servlets;

import auth.Auth;
import messages.*;
import users.*;
import utils.FreemarkerService;
import utils.exceptions.*;

import javax.servlet.http.*;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

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
        String idToParam = req.getParameter("id");
        UUID idTo = UUID.fromString(idToParam);
        User chatWithUser;
        try {
            chatWithUser = userService.get(idTo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (UserNotFoundException e) {
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
        String idToParam = req.getParameter("id");
        UUID idTo = UUID.fromString(idToParam);

        String messageText = req.getParameter("messageText");
        UUID fromUserId = idFrom;
        UUID toUserId = idTo;
        Message message = new Message(fromUserId, toUserId, messageText);
        System.out.println(resp);
        try {
            if (messageService.addMessage(message)) {
                try {
                    System.out.println(idFrom);
                    System.out.println(idTo);
                    updateChat(resp, idFrom, idTo); // Передаем idFrom и idTo
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (UserNotFoundException e) {
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
