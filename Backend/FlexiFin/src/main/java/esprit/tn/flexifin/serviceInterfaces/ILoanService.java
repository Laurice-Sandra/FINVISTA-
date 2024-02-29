package esprit.tn.flexifin.serviceInterfaces;

import esprit.tn.flexifin.entities.Loan;
import esprit.tn.flexifin.entities.LoanStatus;
import esprit.tn.flexifin.entities.LoanType;
import org.jetbrains.annotations.NotNull;

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

    //Map<String, Float> simulateLoan(float amount, int duration, float interestRate);

    //public double calculateLoanCapacity(double monthlyIncome, double monthlyDebtPayments, double monthlyExpenses);

    Map<String, Float> simulateLoan(Loan loan);

     Map<String, Float> simulateLoan2( Loan loan);
    Map<String, Float> simulateLoanWithConstantAmortizationPerYear( Loan loan);
    Map<String, Float> simulateLoanWithConstantAmortizationPerMonth(Loan loan);

    Map<String, Float> simulateLoanInFineByYear( Loan loan);
    Map<String, Float> simulateLoanInFineByMonth( Loan loan);



    void generatePdf(LinkedHashMap<String, Float> loanSimulation) throws IOException;

    List<Loan> getLoanByStatus(LoanStatus status);

    List<Loan> getLoanByStartDate(LocalDate date);
    List<Loan> getLoanByLoanType(LoanType loantype);

    public List<Loan> getLoanByUserId(Long idUser);

}
