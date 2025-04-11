package Service;

import java.util.List;

import DAO.*;
import Model.*;

public class MessageService {

    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService(){
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    /**
     * Create a new message
     * @param message The message to create
     * @return The created message with ID or null if creation failed
     */
    public Message createMessage(Message message) {
        //validation
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()) {
            return null; 
        }

        if (message.getMessage_text().length() > 255) {
            return null; 
        }

        Account account = accountDAO.getAccountById(message.getPosted_by());
        if (account == null) {
            return null; 
        }

        if (message.getTime_posted_epoch() == 0) {
            message.setTime_posted_epoch(System.currentTimeMillis());
        }
        
        return messageDAO.createMessage(message);
    }

     /**
     * Get all messages
     * @return List of all messages
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
        
    }
    
    /**
     * Get a message by ID
     * @param messageId The message ID
     * @return The message if found, null otherwise
     */
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }
    
    /**
     * Delete a message by ID
     * @param messageId The message ID to delete
     * @return The deleted message or null if none existed
     */
    public Message deleteMessageById(int messageId) {
        return messageDAO.deleteMessageById(messageId);
    }
    
    /**
     * Update a message's text
     * @param messageId The message ID to update
     * @param newMessageText The new message text
     * @return The updated message or null if update failed
     */
    public Message updateMessageText(int messageId, String newMessageText) {
        if (newMessageText == null || newMessageText.trim().isEmpty()) {
            return null; 
        }
        
        if (newMessageText.length() > 255) {
            return null; 
        }
        
        Message existingMessage = messageDAO.getMessageById(messageId);
        if (existingMessage == null) {
            return null; 
        }
        
        return messageDAO.updateMessageText(messageId, newMessageText); 
    }
    
    /**
     * Get all messages from a user
     * @param accountId The account ID
     * @return List of messages posted by the user
     */
    public List<Message> getMessagesByAccount(int accountId) {
        return messageDAO.getMessagesByAccount(accountId);
    }
    
}
