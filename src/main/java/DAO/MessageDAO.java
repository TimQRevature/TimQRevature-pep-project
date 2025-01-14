package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    
    public Message insertMessage(Message msg){
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?);";
            PreparedStatement prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prep.setInt(1, msg.getPosted_by());
            prep.setString(2, msg.getMessage_text());
            prep.setLong(3, msg.getTime_posted_epoch());

            prep.executeUpdate();
            ResultSet rs = prep.getGeneratedKeys();
            if(rs.next()){
                int generatedId = rs.getInt(1);
                msg.setMessage_id(generatedId);
            }
            return msg;
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages(){
        Connection conn = ConnectionUtil.getConnection();
        List<Message> msgs = new ArrayList<Message>();
        try {
            String sql = "SELECT * FROM message;";
            PreparedStatement prep = conn.prepareStatement(sql);

            ResultSet rs = prep.executeQuery();
            while(rs.next()){
                msgs.add(new Message(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getLong(4)));
            }
            return msgs;
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
        return null;
    }

    public Message getMessageById(int id){
        Connection conn = ConnectionUtil.getConnection();
        Message msg;
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement prep = conn.prepareStatement(sql);
            prep.setInt(1, id);

            ResultSet rs = prep.executeQuery();
            while(rs.next()){
                msg = new Message(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getLong(4));
                return msg;
            }
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
        return null;
    }

    public void deleteMessageById(int id){
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prep.setInt(1, id);
            prep.executeUpdate();
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
    }

    public boolean updateMessageById(String newMsgText, int id){
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement prep = conn.prepareStatement(sql);
            prep.setString(1, newMsgText);
            prep.setInt(2, id);
            int rowsAffected = prep.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.print(e.getMessage());
            return false;
        }
    }

    public List<Message> getMessagesByAccountId(int accId){
        Connection conn = ConnectionUtil.getConnection();
        List<Message> msgs = new ArrayList<Message>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement prep = conn.prepareStatement(sql);
            prep.setInt(1, accId);

            ResultSet rs = prep.executeQuery();
            while(rs.next()){
                msgs.add(new Message(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getLong(4)));
            }
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
        return msgs;
    }
}
