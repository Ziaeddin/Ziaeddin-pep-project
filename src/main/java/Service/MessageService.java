package Service;


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
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()) {
            return null; // Message text can't be blank
        }
        
        if (message.getMessage_text().length() > 255) {
            return null; // Message text can't be over 255 characters
        }
        
        // Validate that the user exists
        Account account = accountDAO.getAccountById(message.getPosted_by());
        if (account == null) {
            return null; // User doesn't exist
        }
        
        // If timestamp is not set, set it to current time
        if (message.getTime_posted_epoch() == 0) {
            message.setTime_posted_epoch(System.currentTimeMillis());
        }
        
        // Create the message
        return messageDAO.createMessage(message);
    }
    
}
