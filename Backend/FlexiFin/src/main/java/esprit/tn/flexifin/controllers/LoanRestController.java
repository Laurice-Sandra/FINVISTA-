package esprit.tn.flexifin.controllers;

import esprit.tn.flexifin.entities.Loan;
import esprit.tn.flexifin.entities.LoanStatus;
import esprit.tn.flexifin.entities.LoanType;
import esprit.tn.flexifin.serviceInterfaces.ILoanService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/loan")
public class LoanRestController {
    private ILoanService iLoanService;
    //CRUD

    @GetMapping("/GetAllLoan")
    //@PreAuthorize("hasAnyAuthority('SCOPE_USER')")(what is preauthorized?)
    public List<Loan> getallloan (){
        return iLoanService.retrieveAllLoans();
    }

    @PostMapping("/addLoan")
    //@PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public Loan addLoan(@RequestBody Loan  loan) {
        return this.iLoanService.addLoan(loan);
    }
    @PostMapping("addLoan/{idAc}")
    public Loan addLoanAssignAccount(@RequestBody Loan loan,@PathVariable("idAc") Long idAccount) {
        return iLoanService.addLoanAssignAccount(loan, idAccount);
    }

    @GetMapping("/getLoan/{idLoan}")
    //@PreAuthorize("hasAnyAuthority('SCOPE_USER')")
    public Loan getLoan(@PathVariable("idLoan") long id) {
        return this.iLoanService.retrieveLoan(id);
    }

    @PutMapping("/UpdateLoan")
    //@PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public Loan updateLoan(@RequestBody Loan loan) {
        return this.iLoanService.updateLoan(loan);
    }

    //FILTERS
    @GetMapping("/getLoanbystatus/{status}")
    public List<Loan> getLoanByStatus(@PathVariable("status") LoanStatus status) {
        return iLoanService.getLoanByStatus(status);
    }
    @GetMapping("/getLoanbyStartDate/{date}")
    public List<Loan> getLoanByStartDate(@PathVariable("date") LocalDate date) {
        return iLoanService.getLoanByStartDate(date);
    }
    @GetMapping("/getLoanbyType/{type}")
    public List<Loan> getLoanByLoanType(@PathVariable("type") LoanType loantype) {
        return iLoanService.getLoanByLoanType(loantype);
    }

    @GetMapping("/getLoanbyUser/{iduser}")
    public List<Loan> getLoansByUserId(@PathVariable("iduser") Long idUser) {
        return iLoanService.getLoanByUserId(idUser);
    }

    //SIMULATE SERVICES
    @PostMapping("/simulate")
    public Map<String, Float> simulateLoan(@RequestBody Loan loan) {
        return iLoanService.simulateLoan(loan);
    }
    @PostMapping("/generatepdf")
    public void generatePdf(@RequestBody LinkedHashMap<String, Float> loanSimulation) throws IOException {
        iLoanService.generatePdf(loanSimulation);
    }
@DeleteMapping("/deleteLoan/{idLoan}")
    public void removeLoan(@PathVariable("idLoan") Long idLoan) {
        iLoanService.removeLoan(idLoan);
    }
    @PostMapping("/simulate2")
    public Map<String, Float> simulateLoan2(@RequestBody Loan loan) {
        return iLoanService.simulateLoan2(loan);
    }
    @PostMapping("/simulateAY")
    public Map<String, Float> simulateLoanWithConstantAmortizationPerYear(@RequestBody Loan loan) {
        return iLoanService.simulateLoanWithConstantAmortizationPerYear(loan);
    }
    @PostMapping("/simulateAM")
    public Map<String, Float> simulateLoanWithConstantAmortizationPerMonth(@RequestBody Loan loan) {
        return iLoanService.simulateLoanWithConstantAmortizationPerMonth(loan);
    }
    @PostMapping("/simulateIfYear")
    public Map<String, Float> simulateLoanInFineByYear(@RequestBody Loan loan) {
        return iLoanService.simulateLoanInFineByYear(loan);
    }
    @PostMapping("/simulateIfMonth")
    public Map<String, Float> simulateLoanInFineByMonth(@RequestBody Loan loan) {
        return iLoanService.simulateLoanInFineByMonth(loan);
    }
}