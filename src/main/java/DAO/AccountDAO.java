package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;


public class AccountDAO {
    public Account insertAccount(Account acc){
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prep.setString(1,acc.getUsername());
            prep.setString(2,acc.getPassword());
            prep.executeUpdate();

            ResultSet rs = prep.getGeneratedKeys();
            if(rs.next()){
                int generatedId = rs.getInt(1);
                acc.setAccount_id(generatedId);
            }
            return acc;
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
        return null;
    }
    
    public Account getAccountById(int id){
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE account_id = ?;";
            PreparedStatement prep = conn.prepareStatement(sql);
            prep.setInt(1,id);
            ResultSet rs = prep.executeQuery();
            while(rs.next()){
                Account acc = new Account(rs.getInt(1), rs.getString(2), rs.getString(3));
                return acc;
            }
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
        return null;
    }

    public Account getAccountByUsername(String username){
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE username = ?;";
            PreparedStatement prep = conn.prepareStatement(sql);
            prep.setString(1,username);
            ResultSet rs = prep.executeQuery();
            while(rs.next()){
                Account acc = new Account(rs.getInt(1), rs.getString(2), rs.getString(3));
                return acc;
            }
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
        return null;
    }


}
