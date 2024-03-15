package esprit.tn.flexifin.serviceImp;

import esprit.tn.flexifin.entities.Loan;
import esprit.tn.flexifin.repositories.LoanRepository;
import esprit.tn.flexifin.serviceInterfaces.ILoanService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class LoanServiceImp implements ILoanService {
    LoanRepository loanRepository;

    public List<Loan> retrieveAllLoans(){
        return loanRepository.findAll();
    }

    public Loan addLoan(Loan loan){
        return loanRepository.save(loan);
    }

    public  Loan updateLoan (Loan loan){
//        Optional<Loan> contratOptional = loanRepository.findById(loan.getIdLoan());
//        if (contratOptional.isPresent()) {
//            Loan loanToUpdate = contratOptional.orElse(null);
//            loanToUpdate.setLoanStatus(LoanStatus.InProgress);
//            return loanRepository.save(loanToUpdate);
//        }

        return loanRepository.save(loan);
    }

    @Override
    public Loan retrieveLoan(Long idLoan) {
        return null;
    }

    @Override
    public void removeLoan(Long idLoan) {

    }


    @Override
    public List<Loan> getLoanByStartDate(LocalDate date) {
        return null;
    }

    @Override
    public List<Loan> getLoanByUserId(Long idUser) {
        return null;
    }


}
