package esprit.tn.flexifin.controllers;

import esprit.tn.flexifin.entities.Loan;
import esprit.tn.flexifin.serviceInterfaces.ILoanService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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



}