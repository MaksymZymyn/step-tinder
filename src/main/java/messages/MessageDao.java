package messages;

import java.util.List;
import java.util.UUID;

public interface MessageDao {
    List<Message> ReadMessageFromDialog(UUID fromUserId, UUID toUserId);
    boolean addMessage(Message message);
    boolean deleteMessage(UUID messageId);
}
