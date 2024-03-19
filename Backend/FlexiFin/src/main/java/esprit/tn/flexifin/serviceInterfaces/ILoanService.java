package esprit.tn.flexifin.serviceInterfaces;
import com.itextpdf.text.DocumentException;
import esprit.tn.flexifin.entities.Loan;
import esprit.tn.flexifin.entities.LoanStatus;
import esprit.tn.flexifin.entities.LoanType;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


public interface ILoanService {
    //void sendEmailWithAttachment(String to, String subject, String body, String attachmentPath) throws MessagingException;

    List<Loan> retrieveAllLoans();

    Loan addLoan(Loan loan);
    Loan addLoanAssignAccount(Loan loan, Long idAccount);

    Loan updateLoan (Loan loan);

    Loan retrieveLoan (Long idLoan);

    void removeLoan(Long idLoan);

    public double calculateLoanCapacity(double monthlyIncome, double monthlyDebtPayments, double monthlyExpenses);

    List<Loan> getLoanByStatus(LoanStatus status);

    List<Loan> getLoanByStartDate(LocalDate date);
    List<Loan> getLoanByLoanType(LoanType loantype);

    public List<Loan> getLoanByUserId(Long idUser);



    String createLoanSimulationPdf(Loan loan) throws DocumentException, FileNotFoundException;


    List<String[]> simulateLoan(Loan loan);

    void updateTmm(float newTmm);

    double calculatePayment(float amount, float interestRate, int totalPeriods);

    String approveLoan(Long loanId) throws DocumentException, FileNotFoundException;
    void updatePaymentDueDates();

    public void processPendingLoansUpdated() throws DocumentException, MessagingException, IOException, TemplateException;
    void confirmLoan(Long idLoan);

    //String approveLoanById(Long loanId) throws DocumentException, FileNotFoundException, MessagingException;

    public String approveLoanByIdWithFreemarker(Long loanId) throws DocumentException, MessagingException, IOException, TemplateException;

}
