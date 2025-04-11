package DAO;

import java.sql.*;
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

        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                message.setMessage_id(generatedId);
                return message;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Gets all messages from the database
     * @return List of all messages
     */
    public List<Message> getAllMessages() {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message";
        List<Message> messages = new ArrayList<>();
        
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                int messageId = rs.getInt("message_id");
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePostedEpoch = rs.getLong("time_posted_epoch");
                
                messages.add(new Message(messageId, postedBy, messageText, timePostedEpoch));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return messages;
    }

    /**
     * Gets a specific message by ID
     * @param messageId The message ID to search for
     * @return The found message or null if none exists
     */
    public Message getMessageById(int messageId) {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE message_id = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, messageId);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePostedEpoch = rs.getLong("time_posted_epoch");
                
                return new Message(messageId, postedBy, messageText, timePostedEpoch);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Deletes a message
     * @param messageId The message ID to delete
     * @return The deleted message or null if none existed
     */
    public Message deleteMessageById(int messageId) {
        // First get the message
        Message message = getMessageById(messageId);
        if (message == null) {
            return null;
        }
        
        // Then delete it
        Connection conn = ConnectionUtil.getConnection();
        String sql = "DELETE FROM message WHERE message_id = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, messageId);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return message;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Updates a message's text
     * @param messageId The message ID to update
     * @param newMessageText The new message text
     * @return The updated message or null if none existed
     */
    public Message updateMessageText(int messageId, String newMessageText){
        Connection conn = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newMessageText);
            ps.setInt(2, messageId);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return getMessageById(messageId);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Gets all messages from a specific user
     * @param accountId The account ID to get messages from
     * @return List of messages posted by the specified user
     */
    public List<Message> getMessagesByAccount(int accountId) {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE posted_by = ?";
        List<Message> messages = new ArrayList<>();
        
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, accountId);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int messageId = rs.getInt("message_id");
                String messageText = rs.getString("message_text");
                long timePostedEpoch = rs.getLong("time_posted_epoch");
                
                messages.add(new Message(messageId, accountId, messageText, timePostedEpoch));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return messages;
    }
    
}
