package messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import utils.exceptions.InvalidMessageDataException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class Message {
    private UUID messageId;
    private UUID fromUserId;
    private UUID toUserId;
    private String messageText;
    private Long time;

    public Message(UUID fromUserId, UUID toUserId, String messageText) {
        this(null, fromUserId, toUserId, messageText, null);
    }

    public static Message fromRS(ResultSet rs) throws SQLException, InvalidMessageDataException {
        UUID messageId = rs.getObject("messageId", UUID.class);
        UUID fromUserId = rs.getObject("fromUserId", UUID.class);
        UUID toUserId = rs.getObject("toUserId", UUID.class);
        String messageText = rs.getString("messageText");
        Long time = rs.getLong("time");

        if (messageId == null || fromUserId == null || toUserId == null || messageText == null || time == null)
            throw new InvalidMessageDataException();

        return new Message(messageId, fromUserId, toUserId, messageText, time);
    }

}
