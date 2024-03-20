package messages;

import lombok.*;
import users.User;
import utils.exceptions.InvalidMessageDataException;
import utils.exceptions.InvalidUserDataException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

@AllArgsConstructor
@Getter
@Setter
public class Message {
    private UUID messageId;
    private UUID fromUserId;
    private UUID toUserId;
    private String messageText;
    private long time;

    public Message(UUID messageId, UUID fromUserId, UUID toUserId, String messageText, long time) {
        this.messageId = messageId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.messageText = messageText;
        this.time = time;
    }

  public Message(UUID fromUserId, UUID toUserId, String messageText) {
        this.messageId = UUID.randomUUID();
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.messageText = messageText;
        this.time = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();;
    }

    public static Message fromRS(ResultSet rs) throws SQLException, InvalidMessageDataException {
        UUID messageId = rs.getObject("messageId", UUID.class);
        UUID fromUserId = rs.getObject("fromUserId", UUID.class);
        UUID toUserId = rs.getObject("toUserId", UUID.class);
        String messageText = rs.getString("messageText");
        Long time = rs.getLong("time");

        if (messageId == null || fromUserId == null || toUserId == null || messageText == null || time == null) throw new InvalidMessageDataException();

        return new Message(messageId, fromUserId, toUserId, messageText, time);
    }

    public static String formatUnixTimestamp(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, HH:mm", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", messageText='" + messageText + '\'' +
                ", time=" + time +
                '}';
    }
}
