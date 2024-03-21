package esprit.tn.flexifin.serviceInterfaces;

import com.stripe.exception.StripeException;
import esprit.tn.flexifin.entities.Account;
import esprit.tn.flexifin.entities.Transaction;

import java.util.List;

public interface IAccountService {
    List<Account> retrieveAllAccounts();

    Account addAccountForProfile(Long profileId, Account account);

    Account addAccount(Account account);

    Account updateAccount (Account account);

    Account retrieveAccount (Long idAccount);

   Transaction processTransactionAndAdjustBalance(Long senderAccountId, Long receiverAccountId, Transaction transaction) throws StripeException;
}
