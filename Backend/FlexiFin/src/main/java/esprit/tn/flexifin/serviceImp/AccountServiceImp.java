package esprit.tn.flexifin.serviceImp;

import esprit.tn.flexifin.entities.Account;
import esprit.tn.flexifin.repositories.AccountRepository;
import esprit.tn.flexifin.serviceInterfaces.IAccountService;
import esprit.tn.flexifin.serviceInterfaces.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceImp implements IAccountService {
    AccountRepository accountRepository;
    @Override
    public List<Account> retrieveAllAccounts(){
        return accountRepository.findAll();
    }
    @Override
    public Account addAccount(Account account){
        return accountRepository.save(account);
    }
    @Override
    public Account updateAccount (Account account){
        return accountRepository.save(account);
    }
    @Override
    public Account retrieveAccount (Long idAccount){
        return accountRepository.findById(idAccount).orElse(null);

    }
}
