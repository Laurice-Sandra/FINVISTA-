package esprit.tn.flexifin.controllers;

import com.stripe.exception.StripeException;
import esprit.tn.flexifin.entities.Account;
import esprit.tn.flexifin.entities.Transaction;
import esprit.tn.flexifin.serviceInterfaces.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/account")
public class AccountRestController {
    @GetMapping("/GetAllAccount")
    public List<Account> retrieveAllAccounts() {
        return iAccountService.retrieveAllAccounts();
    }
    @PostMapping("/addAccount")
    public Account addAccount(@RequestBody Account account) {
        return iAccountService.addAccount(account);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable(value = "id") Long accountId,
                                                 @RequestBody Account accountDetails) {
        Account updatedAccount = iAccountService.updateAccount(accountId, accountDetails);
        if (updatedAccount == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedAccount);
    }

    public Account retrieveAccount(Long idAccount) {
        return iAccountService.retrieveAccount(idAccount);
    }

    IAccountService iAccountService;
@PostMapping("addAccount/{idP}")
    public Account addAccountForProfile(@PathVariable("idP") Long profileId, @RequestBody Account account) {
        return iAccountService.addAccountForProfile(profileId, account);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable(value = "id") Long accountId) {
        iAccountService.deleteAccount(accountId);
        return ResponseEntity.ok().build();
    }


}
