package esprit.tn.flexifin.serviceInterfaces;

import com.stripe.exception.StripeException;
import esprit.tn.flexifin.entities.Account;
import esprit.tn.flexifin.entities.Transaction;

import java.util.List;

public interface IAccountService {
    List<Account> retrieveAllAccounts();

    void deleteAccount(Long accountId);

    Account addAccountForProfile(Long profileId, Account account);

    Account addAccount(Account account);



    Account updateAccount(Long id, Account accountDetails);

    Account retrieveAccount (Long idAccount);

    /*Transaction processTransactionAndAdjustBalance(Long senderAccountId, Long receiverAccountId, Transaction transaction) throws StripeException;

    Transaction processPaymentToReceiver(Long receiverAccountId, Transaction paymentRequest) throws StripeException;*/
}
