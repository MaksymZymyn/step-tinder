package messages;

import lombok.*;
import utils.exceptions.InvalidMessageDataException;

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

    public Message(UUID fromUserId, UUID toUserId, String messageText) {
        this(null, fromUserId, toUserId, messageText, LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
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
}
