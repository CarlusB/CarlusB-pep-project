package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    public Message createMessage(Message newMessage){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            Boolean blankMessage = newMessage.getMessage_text().isBlank();
            Boolean messageTooLong = newMessage.getMessage_text().length() > 255;
            String existSql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement existingAccountStatement = connection.prepareStatement(existSql);
            existingAccountStatement.setInt(1, newMessage.getPosted_by());
            Boolean accountExists = existingAccountStatement.execute();

            if(blankMessage || messageTooLong || !accountExists){
                return null;
            }

            preparedStatement.setInt(1, newMessage.getPosted_by());
            preparedStatement.setString(2, newMessage.getMessage_text());
            preparedStatement.setLong(3, newMessage.getTime_posted_epoch());
            preparedStatement.executeUpdate();

            ResultSet pKeyResultSet = preparedStatement.getGeneratedKeys(); 
            
            if(pKeyResultSet.next()){
                int generated_message_id = pKeyResultSet.getInt(1);
                return new Message(
                    generated_message_id,
                    newMessage.getPosted_by(),
                    newMessage.getMessage_text(),
                    newMessage.getTime_posted_epoch()
                    );
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return null;
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<Message>();

        try{
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
        
            if(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
                return messages; 
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return messages;
    }

    public Message getMessageByID(int messageID){
        Connection connection = ConnectionUtil.getConnection();
        Message message = new Message();

        try{
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, messageID);
            ResultSet rs = preparedStatement.executeQuery();
            
            if(rs.next()){
                message.setMessage_id(rs.getInt("message_id"));
                message.setPosted_by(rs.getInt("posted_by"));
                message.setMessage_text(rs.getString("message_text"));
                message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message deleteMessageByID(int messageID){
        Connection connection = ConnectionUtil.getConnection();
        Message message = new Message();
        try{
            String deleteSql = "DELETE FROM message WHERE message_id = ?";
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSql);
            PreparedStatement tempStatement = connection.prepareStatement(selectSql);

            tempStatement.setInt(1, messageID);
            ResultSet rs = tempStatement.executeQuery();

            preparedStatement.setInt(1, messageID);
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected == 0){
                return null;
            }
            
            if(rs.next()){
                message.setMessage_id(rs.getInt("message_id"));
                message.setPosted_by(rs.getInt("posted_by"));
                message.setMessage_text(rs.getString("message_text"));
                message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message updateMessageByID(int messageID, Message messageText){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement tempStatement = connection.prepareStatement(selectSql);

            Boolean blankMessage = messageText.getMessage_text().isBlank();
            Boolean messageTooLong = messageText.getMessage_text().length() > 255;
            if(blankMessage || messageTooLong){
                return null;
            }

            preparedStatement.setString(1, messageText.getMessage_text());
            preparedStatement.setInt(2, messageID);
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected == 0){
                return null;
            }

            tempStatement.setInt(1, messageID);
            ResultSet rs = tempStatement.executeQuery();
            
            if(rs.next()){
                return new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                ); 
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Message> getUserMessages(int accountID){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<Message>();
        
        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, accountID);
            ResultSet rs = preparedStatement.executeQuery();
            
            if(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
                return messages;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return messages;
    }
    
}
