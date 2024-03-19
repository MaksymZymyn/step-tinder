package servlets;

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

    UUID idFrom = UUID.fromString("8223ae18-66c5-4960-9074-8e5ce8f3c776");
    UUID idTo = UUID.fromString("52b876c6-72e5-4b80-b704-3de8cf8b1e27");

    public MessagesServlet() throws IOException {
        this.messageService = new MessageService(new MessageDAO());
        this.userService = new UserService(new UserDAO());
        this.freemarker = new FreemarkerService("templates");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

//        UUID idFrom = UUID.fromString("8223ae18-66c5-4960-9074-8e5ce8f3c776");
//        UUID idTo = UUID.fromString("52b876c6-72e5-4b80-b704-3de8cf8b1e27");
        User chatWithUser;
        try {
            chatWithUser = userService.get(idTo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<Message> messages =  messageService.ReadMessageFromDialog(idFrom, idTo);

        HashMap<String, Object> templateData = new HashMap<>();

        templateData.put("chatWithUser", chatWithUser); // Передаємо об'єкт користувача
        templateData.put("messages", messages); // Передаємо список повідомлень

        try (PrintWriter w = resp.getWriter()) {
            freemarker.render("chat.ftl", templateData, w);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String messageText = req.getParameter("messageText");
        UUID fromUserId = idFrom;
        UUID toUserId = idTo;
        Message message = new Message(fromUserId, toUserId, messageText);
        try {
            if (messageService.addMessage(message)) {
                try {
                    updateChat(resp);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (InvalidUserDataException | UserNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateChat(HttpServletResponse resp) throws SQLException, InvalidUserDataException, UserNotFoundException {
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
