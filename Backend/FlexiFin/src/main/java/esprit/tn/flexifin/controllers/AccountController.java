package esprit.tn.flexifin.controllers;

import esprit.tn.flexifin.entities.Account;
import esprit.tn.flexifin.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/accounts")
public class AccountController {


    AccountService accountService;


    @PostMapping("/")
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }


    @GetMapping("/")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable(value = "id") Long accountId) {
        Account account = accountService.getAccountById(accountId);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(account);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable(value = "id") Long accountId,
                                                 @RequestBody Account accountDetails) {
        Account updatedAccount = accountService.updateAccount(accountId, accountDetails);
        if (updatedAccount == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedAccount);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable(value = "id") Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok().build();
    }
}
