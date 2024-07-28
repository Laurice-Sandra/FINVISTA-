package esprit.tn.flexifin.serviceImp;

import esprit.tn.flexifin.entities.Account;
import esprit.tn.flexifin.entities.Profile;
import esprit.tn.flexifin.repositories.AccountRepository;
import esprit.tn.flexifin.repositories.ProfileRepository;
import esprit.tn.flexifin.serviceInterfaces.IAccountService;
import esprit.tn.flexifin.serviceInterfaces.ITransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceImp implements IAccountService {
    AccountRepository accountRepository;
    ProfileRepository profileRepository;
    ITransactionService transactionService;

    @Override
    public List<Account> retrieveAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account addAccount(Account account) {
        return accountRepository.save(account);
    }


    @Override
    public Account updateAccount(Long id, Account accountDetails) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found for this id :: " + id));
        account.setBalance(accountDetails.getBalance());
        account.setOpenDate(accountDetails.getOpenDate());
        final Account updatedAccount = accountRepository.save(account);
        return updatedAccount;
    }

    @Override
    public Account retrieveAccount(Long idAccount) {
        return accountRepository.findById(idAccount).orElse(null);

    }
    @Override
    public void deleteAccount(Long accountId){
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found for this id :: " + accountId));
        accountRepository.delete(account);

    }

    public Account addAccountForProfile(Long profileId, Account account) {
        Profile profile = profileRepository.findById(profileId).orElse(null);
        account.setProfile(profile);
        return accountRepository.save(account);
    }

   /* @Override
    @Transactional
    public Transaction processTransactionAndAdjustBalance(Long senderAccountId, Long receiverAccountId, Transaction transaction) throws StripeException {
        // Récupérer les comptes de l'expéditeur et du destinataire
        Account senderAccount = accountRepository.findById(senderAccountId).orElse(null);
        Account receiverAccount = accountRepository.findById(receiverAccountId).orElse(null);
        // Vérifier si le solde du compte expéditeur est suffisant
        if (senderAccount.getBalance() < transaction.getAmount()) {
            throw new IllegalArgumentException("Insufficient balance in sender's account");
        }

        // Effectuer la transaction
        Transaction completedTransaction = transactionService.processPayment(transaction);

        // Vérifier si la transaction a été complétée avec succès
        if (completedTransaction.getStatus() == TranStatus.COMPLETED ) {
            // Réduire le solde du compte expéditeur
            senderAccount.setBalance(senderAccount.getBalance() - transaction.getAmount());

            // Augmenter le solde du compte destinataire
            receiverAccount.setBalance(receiverAccount.getBalance() + transaction.getAmount());

            // Sauvegarder les modifications dans la base de données
            accountRepository.save(senderAccount);
            accountRepository.save(receiverAccount);
        } else {
            throw new IllegalStateException("Transaction did not complete successfully");
        }

        return completedTransaction; // Retourner le compte expéditeur mis à jour
    }

    @Override
    public Transaction processPaymentToReceiver(Long receiverAccountId, Transaction paymentRequest) throws StripeException {
        Account receiverAccount = accountRepository.findById(receiverAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver account not found"));

        // Process the payment through the TransactionService
        Transaction processedTransaction = transactionService.processPayment(paymentRequest);

        // Update the receiver account's balance
        receiverAccount.setBalance(receiverAccount.getBalance() + paymentRequest.getAmount());
        accountRepository.save(receiverAccount);
        processedTransaction.setReceiverAccount(receiverAccount);
        // Assuming you're setting a default or null value for sender account or handling it accordingly
        return processedTransaction;
    }*/




}
