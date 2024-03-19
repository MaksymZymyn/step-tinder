package messages;

import java.util.*;

public class MessageService {
    private MessageDAO dao;
    public MessageService(MessageDAO dao) {
        this.dao = dao;
    }
    public List<Message> ReadMessageFromDialog(UUID fromUserId, UUID toUserId) {
        List<Message> messages = dao.get(fromUserId, toUserId);
        Collections.sort(messages, Comparator.comparingLong(Message::getTime));
        return messages;
    }
    public boolean addMessage(Message message){
        return dao.insert(message);
    }


}
