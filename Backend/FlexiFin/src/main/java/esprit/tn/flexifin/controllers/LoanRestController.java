package esprit.tn.flexifin.controllers;

import com.itextpdf.text.DocumentException;
import com.stripe.exception.StripeException;
import esprit.tn.flexifin.entities.Loan;
import esprit.tn.flexifin.entities.LoanStatus;
import esprit.tn.flexifin.entities.LoanType;
import esprit.tn.flexifin.entities.Transaction;
import esprit.tn.flexifin.serviceInterfaces.ILoanService;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
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
    public String updateTmm(@PathVariable("tmm") double newTmm) {
        iLoanService.updateTmm(newTmm);
        return "TMM updated successfully to " + newTmm;
    }

    // Endpoint temporaire pour tester la mise à jour des dates d'échéance
    @GetMapping("/triggerUpdatePaymentDueDates")
    public ResponseEntity<String> triggerUpdatePaymentDueDates() {
        iLoanService.updatePaymentDueDates();
        return ResponseEntity.ok("Payment due dates update triggered.");
    }
    @GetMapping("/confirm-loan/{loanId}")
    public ResponseEntity<String> confirmLoan(@PathVariable("loanId") Long loanId) {
        iLoanService.confirmLoan(loanId);
        return ResponseEntity.ok("Prêt confirmé avec succès.");
    }


    @PutMapping("approveloanEmailFM/{id}")
    public String approveLoanByIdWithFreemarker(@PathVariable("id") Long loanId) throws DocumentException, MessagingException, IOException, TemplateException {
        return iLoanService.approveLoanByIdWithFreemarker(loanId);
    }
    @GetMapping("/pendingLoansUpdate")
    public ResponseEntity<String> processPendingLoansUpdated() throws DocumentException, MessagingException, IOException, TemplateException {
        iLoanService.processPendingLoansUpdated();
        return ResponseEntity.ok("Pending loans updated with success");
    }
    @PostMapping("/loanTransaction/{send}/{receive}/{idL}")
    public Transaction processLoanTransactionWithSpecificLoan(@PathVariable("send") Long senderAccountId,@PathVariable("receive") Long receiverAccountId,@RequestBody Transaction paymentRequest, @PathVariable("idL")Long loanId) throws StripeException {
        return iLoanService.processLoanTransactionWithSpecificLoan(senderAccountId, receiverAccountId, paymentRequest, loanId);
    }

    // Endpoint pour tester l'envoi de la notification Twilio
    @GetMapping("/send-reminder/{loanId}")
    public ResponseEntity<String> sendLoanPaymentReminder(@PathVariable Long loanId) {
        try {
            iLoanService.sendPreDueDateNotification(loanId);
            return ResponseEntity.ok("Notification sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send notification: " + e.getMessage());
        }
    }


}