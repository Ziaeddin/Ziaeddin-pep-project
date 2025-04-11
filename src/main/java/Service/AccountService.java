package Service;

import java.sql.SQLException;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    /**
     * Register a new account
     * @param account The account to register
     * @return The registered account with ID or null if registration failed
     */
    public Account register(Account account) {
        try {
            // Validate username and password
            if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
                return null; 
            }
            if (account.getPassword() == null || account.getPassword().length() < 4) {
                return null; 
            }
            Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());
            if (existingAccount != null) {
                return null; // Username already exists
            }
            return accountDAO.createAccount(account);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Login 
     * @param account The account containing login credentials
     * @return The account with ID if login successful, null otherwise
     */
    public Account login(Account account) {
        return accountDAO.getAccountByCredentials(account.getUsername(), account.getPassword());
    }
    
    
}
