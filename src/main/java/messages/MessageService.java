package messages;

import java.text.SimpleDateFormat;
import java.util.*;

public class MessageService {
    private MessageDao dao;
    public MessageService(MessageDao dao) {
        this.dao = dao;
    }
    public List<Message> ReadMessageFromDialog(UUID fromUserId, UUID toUserId) {
        List<Message> messages = dao.ReadMessageFromDialog(fromUserId, toUserId);
        Collections.sort(messages, Comparator.comparingLong(Message::getTime));
        return messages;
    }
    public boolean addMessage(Message message){
        return dao.addMessage(message);
    }
    public boolean deleteMessage(UUID messageId){
        return dao.deleteMessage(messageId);
    }


}
