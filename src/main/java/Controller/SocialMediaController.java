package Controller;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Service.AccountService;
import Service.MessageService;
import Model.Message;

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

    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postUserRegHandler);
        app.post("/login", this::postUserLoginHandler);
        app.post("/messages", this::postMessageCreation);
        app.get("/messages",this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessageById);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccId);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void postUserRegHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account acc = mapper.readValue(ctx.body(), Account.class);
        Account addedAcc = accountService.registerUser(acc);

        if(addedAcc == null){
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(addedAcc));
        }
    }

    private void postUserLoginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account acc = mapper.readValue(ctx.body(), Account.class);
        Account retAcc = accountService.loginUser(acc);

        if(retAcc == null){
            ctx.status(401);
        } else {
            ctx.json(mapper.writeValueAsString(retAcc));
        }
    }

    private void postMessageCreation(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message msg = mapper.readValue(ctx.body(), Message.class);
        Message retMsg = messageService.messageCreation(msg);

        if(retMsg == null){
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(retMsg));
        }
    }

    private void getAllMessages(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        List<Message> msgs = messageService.getAllMessages();
        ctx.json(mapper.writeValueAsString(msgs));
    }
    
    private void getMessageById(Context ctx) throws JsonProcessingException{
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message msg = messageService.getMessageById(id);
        if(msg == null){
            ctx.status(200);
        } else {
            ctx.json(mapper.writeValueAsString(msg));
        }
    }


    private void deleteMessageById(Context ctx) throws JsonProcessingException{
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message msg = messageService.deleteMessageById(id);
        if(msg == null){
            ctx.status(200);
        } else {
            ctx.json(mapper.writeValueAsString(msg));
        }
        
    }

    private void updateMessageById(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("message_id")));
        JsonNode jsonNode = mapper.readTree(ctx.body());
        String msgTextToPatch = jsonNode.get("message_text").asText();

        Message msgRet = messageService.updateMessageById(msgTextToPatch, id);

        if(msgRet == null){
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(msgRet));
        }
    }

    private void getMessagesByAccId(Context ctx) throws JsonProcessingException{
        int id = Integer.parseInt(ctx.pathParam("account_id"));
        ObjectMapper mapper = new ObjectMapper();
        List<Message> msg = messageService.getMessagesByAccId(id);
        ctx.json(mapper.writeValueAsString(msg));
    }


    

}