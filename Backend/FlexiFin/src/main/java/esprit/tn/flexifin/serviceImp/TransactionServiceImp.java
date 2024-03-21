package esprit.tn.flexifin.serviceImp;


import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import esprit.tn.flexifin.entities.Account;
import esprit.tn.flexifin.entities.TranStatus;
import esprit.tn.flexifin.entities.Transaction;
import esprit.tn.flexifin.entities.User;
import esprit.tn.flexifin.repositories.AccountRepository;
import esprit.tn.flexifin.repositories.TransactionRepository;
import esprit.tn.flexifin.serviceInterfaces.ITransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class TransactionServiceImp implements ITransactionService {
    TransactionRepository transactionRepository;
    AccountRepository accountRepository;


    @Override
    public List<Transaction> retrieveAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction addTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction retrieveTransaction(Long idTransaction) {
        return transactionRepository.findById(idTransaction).orElse(null);

    }
@Override

public Transaction processPayments(Transaction paymentRequest, Long senderAccountId, Long receiverAccountId) throws StripeException {
    // Vérifier l'existence des comptes dans la base de données
    Account senderAccount = accountRepository.findById(senderAccountId).orElse(null);
    Account receiverAccount = accountRepository.findById(receiverAccountId).orElse(null);
    if (senderAccount== null || receiverAccount == null) {
        throw new IllegalArgumentException("Sender and receiver accounts must not be null");
    }

    // Préparer les paramètres pour le PaymentIntent
    Map<String, Object> params = new HashMap<>();
    params.put("amount", paymentRequest.getAmount() * 100); // Stripe utilise les plus petites unités de la devise
    params.put("currency", paymentRequest.getCurrency().toString().toLowerCase());

    // Choisir un payment_method en fonction du besoin de simuler un échec


    params.put("payment_method", "pm_card_visa");
    params.put("payment_method_types", Collections.singletonList("card"));

    //recuperation du userid
    User user =senderAccount.getProfile().getUser();

    // Utilisation de métadonnées pour relier le paiement à un utilisateur de votre système
    Map<String, String> metadata = new HashMap<>();
    metadata.put("userId", user.getIdUser().toString());
    metadata.put("userName", user.getFirstName());

    params.put("metadata", metadata);

    // Créer le PaymentIntent avec Stripe
    PaymentIntent paymentIntent = PaymentIntent.create(params);

    paymentIntent.confirm();

    // Rafraîchir les données du PaymentIntent pour obtenir le statut mis à jour
    paymentIntent = PaymentIntent.retrieve(paymentIntent.getId());

    // Mettre à jour la transaction avec les informations de Stripe
    paymentRequest.setPaymentId(paymentIntent.getId());
    paymentRequest.setSenderAccount(senderAccount);
    paymentRequest.setReceiverAccount(receiverAccount);

    switch (paymentIntent.getStatus()) {
        case "succeeded":
            paymentRequest.setStatus(TranStatus.COMPLETED);
            break;
        case "requires_payment_method":
        case "requires_confirmation":
        case "requires_action":
            paymentRequest.setStatus(TranStatus.IN_PROGRESS);
            break;
        case "canceled":
            paymentRequest.setStatus(TranStatus.CANCELLED);
            break;
        default:
            // Gérer d'autres statuts au besoin
            paymentRequest.setStatus(TranStatus.IN_PROGRESS);
    }

    // Sauvegarder la transaction mise à jour dans la base de données
    return transactionRepository.save(paymentRequest);
}

   public Transaction processPayment(Transaction paymentRequest) throws StripeException {

        // Create a PaymentIntent object with the payment details
        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentRequest.getAmount());
        params.put("currency", paymentRequest.getCurrency());
        params.put("payment_method_types", Collections.singletonList("card"));

        params.put("payment_method", "pm_card_visa");
        // params.put("payment_method", "pm_card_amex");

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        // Confirm the PaymentIntent to charge the payment
        paymentIntent.confirm();

        paymentRequest.setPaymentId(paymentIntent.getId());


        // Save the transaction to the database
        return transactionRepository.save(paymentRequest);
    }
}
