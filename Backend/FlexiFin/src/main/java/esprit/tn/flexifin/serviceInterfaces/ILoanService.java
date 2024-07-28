package esprit.tn.flexifin.serviceInterfaces;
import com.itextpdf.text.DocumentException;
import com.stripe.exception.StripeException;
import esprit.tn.flexifin.entities.Loan;
import esprit.tn.flexifin.entities.LoanStatus;
import esprit.tn.flexifin.entities.LoanType;
import esprit.tn.flexifin.entities.Transaction;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public interface ILoanService {

    double updateTmm(double newTmm);

    Loan addLoan(Loan loan);
    Loan addLoanAssignAccount(Loan loan, Long idAccount);

    List<Loan> retrieveAllLoans();

    Loan retrieveLoan (Long idLoan);

    void removeLoan(Long idLoan);

    List<Loan> getLoanByStatus(LoanStatus status);

    List<Loan> getLoanByStartDate(LocalDate date);
    List<Loan> getLoanByLoanType(LoanType loantype);

    public List<Loan> getLoanByUserId(Long idUser);


    double calculatePayment(float amount, double interestRate, int totalPeriods);

    public double calculateLoanCapacity(double monthlyIncome, double monthlyDebtPayments, double monthlyExpenses);


    List<String[]> simulateLoanCombined(Loan loan);

    String createLoanSimulationPdf(Loan loan) throws DocumentException, FileNotFoundException;

    void sendEmailWithFreemarkerTemplate(String to, String subject, Map<String, Object> templateModel, String attachmentPath, String templateName) throws MessagingException, IOException, TemplateException;

    public String approveLoanByIdWithFreemarker(Long loanId) throws DocumentException, MessagingException, IOException, TemplateException;


    void updatePaymentDueDates();

    public void processPendingLoansUpdated() throws DocumentException, MessagingException, IOException, TemplateException;
    void confirmLoan(Long idLoan);



    //Remboursement et Virement de pret (USER-ADMIN account)
    Transaction processLoanTransactionWithSpecificLoan(Long senderAccountId, Long receiverAccountId, Transaction paymentRequest, Long loanId) throws StripeException;


    //NOTIFICATION
    @Scheduled(cron = "* * * * * ?") // Executed every day at noon
    void sendPreDueDateNotifications();

    void sendPreDueDateNotification(Long idLoan);
}
