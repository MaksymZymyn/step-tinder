package servlets;

import freemarker.template.TemplateException;
import messages.*;
import freemarker.template.Template;
import users.User;
import users.UserDAO;
import users.UserService;
import utils.exceptions.InvalidUserDataException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MessagesServlet extends HttpServlet {
    messageDAO messageDao = new messageDAO();
    MessageService messageService = new MessageService(messageDao);
    UserDAO userDao = new UserDAO();
    UserService userService = new UserService(userDao);

    UUID idFrom = UUID.fromString("8223ae18-66c5-4960-9074-8e5ce8f3c776");
    UUID idTo = UUID.fromString("52b876c6-72e5-4b80-b704-3de8cf8b1e27");


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        UUID idFrom = UUID.fromString("8223ae18-66c5-4960-9074-8e5ce8f3c776");
//        UUID idTo = UUID.fromString("52b876c6-72e5-4b80-b704-3de8cf8b1e27");
        User chatWithUser;
        try {
            chatWithUser = userService.get(idTo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InvalidUserDataException e) {
            throw new RuntimeException(e);
        }
        List<Message> messages =  messageService.ReadMessageFromDialog(idFrom, idTo);
        // Створюємо об'єкт Map для передачі даних в шаблон
        Map<String, Object> templateData = new HashMap<>();

        templateData.put("chatWithUser", chatWithUser); // Передаємо об'єкт користувача
        templateData.put("messages", messages); // Передаємо список повідомлень

        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        try {
            Template template = TemplateConfig.getInstance().getTemplate("chat.ftl");
            template.process(templateData, writer);
        } catch (TemplateException e) {
            throw new ServletException("Error processing FreeMarker template", e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String messageText = req.getParameter("messageText");
        UUID fromUserId = idFrom;
        UUID toUserId = idTo;
        Message message = new Message(fromUserId, toUserId, messageText);
        if (messageService.addMessage(message)) {
            try {
                updateChat(resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (InvalidUserDataException e) {
                throw new RuntimeException(e);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void updateChat(HttpServletResponse resp) throws IOException, SQLException, InvalidUserDataException {
        List<Message> messages = messageService.ReadMessageFromDialog(idFrom, idTo);
        User chatWithUser = userService.get(idTo);
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("chatWithUser", chatWithUser);
        templateData.put("messages", messages);
        resp.setContentType("text/html");
        try {
            Template template = TemplateConfig.getInstance().getTemplate("chat.ftl");
            template.process(templateData, resp.getWriter());
        } catch (TemplateException e) {
            throw new IOException("Error processing FreeMarker template", e);
        }
    }
}
