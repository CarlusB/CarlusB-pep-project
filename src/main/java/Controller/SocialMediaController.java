package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getUserMessagesHandler);

        //app.start(8080);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    //postRegisterHandler
    private void postRegisterHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account newAccount = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.register(newAccount);
        if(addedAccount != null){
            ctx.json(addedAccount);
        }else{
            ctx.status(400);
        }
    }

    //postLoginHandler
    private void postLoginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedIn = accountService.login(account);
        if(loggedIn != null){
            ctx.json(mapper.writeValueAsString(loggedIn));
            ctx.status(200);
        }else{
            ctx.status(401);
        }
    }

    //postMessageHandler
    private void postMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message newMessage = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.createMessage(newMessage);
        if(addedMessage != null){
            ctx.json(addedMessage);
        }else{
            ctx.status(400);
        }

    }

    //getAllMessagesHandler
    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException{
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
        
    }

    //getMessageHandler
    private void getMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int messageID = mapper.readValue(ctx.pathParam("message_id"), int.class);
        Message message = messageService.getMessageByID(messageID);
        if (message != null) {
            ctx.json(message);
        }else{
            ctx.json("");
        }
        

    }

    //deleteMessageHandler
    private void deleteMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int messageID = mapper.readValue(ctx.pathParam("message_id"), int.class);
        Message message = messageService.deleteMessageByID(messageID);
        if (message != null) {
            ctx.json(message);
        }else{
            ctx.json("");
        }

    }

    //patchMessageHandler
    private void patchMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int messageID = mapper.readValue(ctx.pathParam("message_id"), int.class);
        Message messageText = mapper.readValue(ctx.body(), Message.class);
        Message message = messageService.updateMessageByID(messageID, messageText);
        if (message != null) {
            ctx.json(message);
        }else{
            ctx.status(400);
        }

    }

    //getUserMessagesHandler
    private void getUserMessagesHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int accountID = mapper.readValue(ctx.pathParam("account_id"), int.class);
        List<Message> messages = messageService.getUserMessages(accountID);
        ctx.json(messages);
        

    }






}