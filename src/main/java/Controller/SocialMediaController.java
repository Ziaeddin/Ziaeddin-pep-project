package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.*;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

    AccountService accountService;
    MessageService msgService;
    ObjectMapper mapper;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.msgService = new MessageService();
        this.mapper = new ObjectMapper();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        //Account Endpoints
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);

        //Message Endpoints
        app.post("/messages", this::createMessagehandler);
        app.get("/messages", this::getAllMsgsHandler);
        app.get("/messages/{message_id}", this::getMsgByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMsgHandler);
        app.patch("/messages/{message_id}", this::updateMsgHandler);
        app.get("/accounts/{account_id}/messages", this::getMsgsByUser);

        return app;
    }


    /**
     * register new account
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void registerHandler(Context ctx) throws JsonProcessingException {
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account registeredAccount = accountService.register(account);
        
        if (registeredAccount != null) {
            ctx.json(registeredAccount);
        } else {
            ctx.status(400);
        }
    }

    /**
     * login
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void loginHandler(Context ctx) throws JsonProcessingException {
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedInAccount = accountService.login(account);
        
        if (loggedInAccount != null) {
            ctx.json(loggedInAccount);
        } else {
            ctx.status(401);
        }
    }


    /**
     * add message
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void createMessagehandler(Context ctx) throws JsonProcessingException{
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message createdMessage = this.msgService.createMessage(message);
        
        if (createdMessage != null) {
            ctx.json(createdMessage);
        } else {
            ctx.status(400);
        }
    }

    /**
     * get all messages
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getAllMsgsHandler(Context ctx) {
        ctx.json(this.msgService.getAllMessages());
    }


    /**
     * get a message by id
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getMsgByIdHandler(Context ctx){
        int msgId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = this.msgService.getMessageById(msgId);

        if (message != null) {
            ctx.json(message);
        } else {
            ctx.json(""); 
        }

    }

    /**
     * delete a message by id
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void deleteMsgHandler(Context ctx) {
        int msgId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = this.msgService.deleteMessageById(msgId);
        
        if (deletedMessage != null) {
            ctx.json(deletedMessage);
        } else {
            ctx.json(""); 
        }
    }

    /**
     * update message_text for a message by id
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void updateMsgHandler(Context ctx) throws JsonProcessingException {
        int msgId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = mapper.readValue(ctx.body(), Message.class);

        Message updatedMessage = msgService.updateMessageText(msgId, message.getMessage_text());
        
        if (updatedMessage != null) {
            ctx.json(updatedMessage);
        } else {
            ctx.status(400);
        }
    }

    /**
     * get all messages by user
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getMsgsByUser(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(this.msgService.getMessagesByAccount(accountId));
    }

}