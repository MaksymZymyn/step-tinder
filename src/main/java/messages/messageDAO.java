package messages;

import database.Database;
import utils.interfaces.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class messageDAO implements DAO<Message> {


    public List<Message> get(UUID fromUserId, UUID toUserId) {
        List<Message> messages = new ArrayList<>();
        try (Connection con = Database.connect()){
            String sql = "SELECT * FROM messages WHERE (user_from=? AND user_to=?) OR ( user_from=? AND user_to=?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, fromUserId);
            ps.setObject(2, toUserId);
            ps.setObject(3, toUserId);
            ps.setObject(4, fromUserId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                UUID messageId = UUID.fromString(rs.getString("id"));
                UUID fromUser = UUID.fromString(rs.getString("user_from"));
                UUID toUser = UUID.fromString(rs.getString("user_to"));
                String messageText = rs.getString("content");
                long time = rs.getLong("time");
                Message message = new Message(messageId, fromUser, toUser, messageText, time);
                messages.add(message);
            }
            return messages;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean insert(Message message) {
        try (Connection con = Database.connect()) {
            String sql = "INSERT INTO messages (id, user_from, user_to, content, time) values (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, message.getMessageId());
            ps.setObject(2, message.getFromUserId());
            ps.setObject(3, message.getToUserId());
            ps.setObject(4, message.getMessageText());
            ps.setObject(5, message.getTime());
            ps.execute();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public List<Message> get(UUID id){
        return new ArrayList<>();
    }
}
