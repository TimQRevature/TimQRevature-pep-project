package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }
    
    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO){
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    public Message messageCreation(Message msg){
        if(msg.getMessage_text() == null)
            return null;

        if(msg.getMessage_text().length() > 255 || msg.getMessage_text().length()<= 0)
            return null;
        
        if(this.accountDAO.getAccountById(msg.getPosted_by()) == null)
            return null;
        
        return this.messageDAO.insertMessage(msg);
    }

    public List<Message> getAllMessages(){
        return this.messageDAO.getAllMessages();
    }

    public Message getMessageById(int id){
        return this.messageDAO.getMessageById(id);
    }

    public Message deleteMessageById(int id){
        Message oldMsg = this.messageDAO.getMessageById(id);
        this.messageDAO.deleteMessageById(id);
        return oldMsg;
    }

    public Message updateMessageById(String newMsg, int id){
        Message oldMsg = this.messageDAO.getMessageById(id);
        if(oldMsg == null)
            return null;

        if(newMsg == null)
            return null;

        if(newMsg.length() > 255 || newMsg.length() <= 0)
            return null;
        
        boolean updateSuccess = this.messageDAO.updateMessageById(newMsg, id);
        
        if (!updateSuccess) 
            return null; 
        
        oldMsg.setMessage_text(newMsg);
        return oldMsg;
    }

    public List<Message> getMessagesByAccId(int accId){
        return this.messageDAO.getMessagesByAccountId(accId);
    }
    
}
