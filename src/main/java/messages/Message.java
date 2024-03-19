package messages;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

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


    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    public UUID getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(UUID fromUserId) {
        this.fromUserId = fromUserId;
    }

    public UUID getToUserId() {
        return toUserId;
    }

    public void setToUserId(UUID toUserId) {
        this.toUserId = toUserId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    public String formatUnixTimestamp(long timestamp) {
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
