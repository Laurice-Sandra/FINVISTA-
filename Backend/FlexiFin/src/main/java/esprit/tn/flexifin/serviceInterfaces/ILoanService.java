package esprit.tn.flexifin.serviceInterfaces;

import esprit.tn.flexifin.entities.Loan;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ILoanService {
    List<Loan> retrieveAllLoans();

    Loan addLoan(Loan loan);

    Loan updateLoan (Loan loan);

    Loan retrieveLoan (Long idLoan);

    void removeLoan(Long idLoan);

    //Map<String, Float> simulateLoan(float amount, int duration, float interestRate);

    //public double calculateLoanCapacity(double monthlyIncome, double monthlyDebtPayments, double monthlyExpenses);

    Map<String, Float> simulateLoan(Loan loan);

    void generatePdf(LinkedHashMap<String, Float> loanSimulation) throws IOException;
}
