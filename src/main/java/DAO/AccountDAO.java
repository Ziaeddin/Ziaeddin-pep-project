package DAO;

import java.sql.*;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    /**
     * Creates a new account in the database
     * @param account Account to be created
     * @return The created account with its generated ID
     */
    public Account createAccount(Account account) {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = (int)rs.getLong(1);
                account.setAccount_id(generatedId);
                return account;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets an account by username
     * @param username The username to search for
     * @return The found account or null if none exists
     */
    public Account getAccountByUsername(String username) {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE username = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int accountId = rs.getInt("account_id");
                String password = rs.getString("password");
                
                return new Account(accountId, username, password);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Gets an account by ID
     * @param accountId The account ID to search for
     * @return The found account or null if none exists
     */
    public Account getAccountById(int accountId) {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE account_id = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, accountId);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                
                return new Account(accountId, username, password);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Checks if an account exists by username and password
     * @param username The username
     * @param password The password
     * @return The found account or null if credentials don't match
     */
    public Account getAccountByCredentials(String username, String password) {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int accountId = rs.getInt("account_id");
                return new Account(accountId, username, password);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
}
