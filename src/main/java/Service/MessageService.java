package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        this.messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message createMessage(Message newMessage){
        return messageDAO.createMessage(newMessage);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageByID(int messageID){
        return messageDAO.getMessageByID(messageID);
    }

    public Message deleteMessageByID(int messageID){
        return messageDAO.deleteMessageByID(messageID);
    }

    public Message updateMessageByID(int messageID, Message messageText){
        return messageDAO.updateMessageByID(messageID, messageText);
    }

    public List<Message> getUserMessages(int accountID){
        return messageDAO.getUserMessages(accountID);
    }
    
}
