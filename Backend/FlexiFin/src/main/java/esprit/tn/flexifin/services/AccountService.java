package esprit.tn.flexifin.services;

import esprit.tn.flexifin.entities.Account;
import esprit.tn.flexifin.repositories.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AccountService {


    private AccountRepository accountRepository;


    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }


    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.orElse(null);
    }


    public Account updateAccount(Long id, Account accountDetails) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found for this id :: " + id));
        account.setBalance(accountDetails.getBalance());
        account.setOpenDate(accountDetails.getOpenDate());
        final Account updatedAccount = accountRepository.save(account);
        return updatedAccount;
    }

    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found for this id :: " + accountId));
        accountRepository.delete(account);
    }
}
