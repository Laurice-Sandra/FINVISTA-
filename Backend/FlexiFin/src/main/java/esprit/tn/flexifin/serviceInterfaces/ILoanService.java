package esprit.tn.flexifin.serviceInterfaces;

import esprit.tn.flexifin.entities.Loan;
import esprit.tn.flexifin.entities.LoanStatus;

import java.time.LocalDate;
import java.util.List;

public interface ILoanService {
    List<Loan> retrieveAllLoans();

    Loan addLoan(Loan loan);

    Loan updateLoan (Loan loan);

    Loan retrieveLoan (Long idLoan);

    void removeLoan(Long idLoan);

    List<Loan> getLoanByStatus(LoanStatus status);

    List<Loan> getLoanByStartDate(LocalDate date);

    public List<Loan> getLoanByUserId(Long idUser);

}
