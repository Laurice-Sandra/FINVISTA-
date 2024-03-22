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
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

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



        // Save the transaction to the database
        return transactionRepository.save(paymentRequest);
    }


}
