package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    
    public Account register(Account newAccount){
        Connection connection = ConnectionUtil.getConnection();
        
        try{
            String sql = "INSERT INTO account(username, password) VAlUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            String existSql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement existingAccountStatement = connection.prepareStatement(existSql);
            existingAccountStatement.setString(1, newAccount.getUsername());
            ResultSet accountExists = existingAccountStatement.executeQuery();
            
            Boolean blankUsername = newAccount.getUsername().isBlank();
            Boolean shortPassword = newAccount.getPassword().length() < 4;

            if( blankUsername || shortPassword || accountExists.next()){
                return null;
            }

            preparedStatement.setString(1, newAccount.getUsername());
            preparedStatement.setString(2, newAccount.getPassword());
            preparedStatement.executeUpdate();

            ResultSet pKeyResultSet = preparedStatement.getGeneratedKeys();
            while(pKeyResultSet.next()){
                int generated_account_id = (int) pKeyResultSet.getInt(1);
                return new Account(generated_account_id, newAccount.getUsername(), newAccount.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return null;
    }

    public Account login(Account account){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            ResultSet rs = preparedStatement.executeQuery();
            
            if(rs.next()){
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }




}
