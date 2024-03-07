package esprit.tn.flexifin.controllers;

import com.itextpdf.text.DocumentException;
import esprit.tn.flexifin.entities.Loan;
import esprit.tn.flexifin.entities.LoanStatus;
import esprit.tn.flexifin.entities.LoanType;
import esprit.tn.flexifin.serviceInterfaces.ILoanService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;


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

@DeleteMapping("/deleteLoan/{idLoan}")
    public void removeLoan(@PathVariable("idLoan") Long idLoan) {
        iLoanService.removeLoan(idLoan);
    }




@PutMapping("/generatetestpdf")
    public String createLoanSimulationPdf(@RequestBody Loan loan) throws DocumentException, FileNotFoundException {
        return iLoanService.createLoanSimulationPdf(loan);
    }



    @PutMapping("/simulateLoan")
    public List<String[]> simulateLoan(@RequestBody Loan loan) {
        return iLoanService.simulateLoan(loan);
    }
    @PostMapping("/updateTmm/{tmm}")
    public String updateTmm(@PathVariable("tmm") float newTmm) {
        iLoanService.updateTmm(newTmm);
        return "TMM updated successfully to " + newTmm;
    }
@PutMapping("loanApproval/{idL}")
    public String approveLoanById(@PathVariable("idL") Long loanId) throws DocumentException, FileNotFoundException {
        return iLoanService.approveLoanById(loanId);
    }
}