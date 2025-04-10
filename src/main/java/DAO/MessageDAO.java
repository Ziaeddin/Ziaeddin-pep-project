package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    /**
     * Creates a new message in the database
     * @param message Message to be created
     */
    public Message createMessage(Message message){
        Connection conn = ConnectionUtil.getConnection();

        String sql = "INSERT INTO messages (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int generatedId = rs.getInt("message_id");
                message.setMessage_id(generatedId);
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }


    /**
     * Gets all messages from the database
     * @return List of all messages
     */
    public List<Message> getAllMessages() throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM messages";
        List<Message> messages = new ArrayList<>();
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                int messageId = rs.getInt("message_id");
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePostedEpoch = rs.getLong("time_posted_epoch");
                
                messages.add(new Message(messageId, postedBy, messageText, timePostedEpoch));
            }
        }
        return messages;
    }
    
}
