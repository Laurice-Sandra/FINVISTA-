package esprit.tn.flexifin.serviceInterfaces;

import com.itextpdf.text.DocumentException;
import esprit.tn.flexifin.entities.Loan;
import esprit.tn.flexifin.entities.LoanStatus;
import esprit.tn.flexifin.entities.LoanType;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ILoanService {
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

    String approveLoanById(Long loanId) throws DocumentException, FileNotFoundException;
    void updatePaymentDueDates();

}
