package esprit.tn.flexifin.serviceInterfaces;

import esprit.tn.flexifin.entities.Account;

import java.util.List;

public interface IAccountService {
    List<Account> retrieveAllAccounts();

    Account addAccountForProfile(Long profileId, Account account);

    Account addAccount(Account account);

    Account updateAccount (Account account);

    Account retrieveAccount (Long idAccount);
}
