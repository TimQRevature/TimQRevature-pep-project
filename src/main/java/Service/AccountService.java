package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }
    
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account registerUser(Account acc){
        if(acc.getUsername().length() == 0)
            return null;

        if(acc.getPassword().length() < 4)
            return null;

        if(this.accountDAO.getAccountByUsername(acc.getUsername()) != null)
            return null;

        return this.accountDAO.insertAccount(acc);
    }

    public Account loginUser(Account acc){
        Account realAcc = this.accountDAO.getAccountByUsername(acc.getUsername());
        // account doesnt exist
        if(realAcc == null)
            return null;

        // passwords dont match
        if(!acc.getPassword().equals(realAcc.getPassword()))
            return null;
        
        return realAcc;
    }

}
