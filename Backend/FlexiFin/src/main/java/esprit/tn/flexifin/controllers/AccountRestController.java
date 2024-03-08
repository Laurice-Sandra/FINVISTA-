package esprit.tn.flexifin.controllers;

import esprit.tn.flexifin.entities.Account;
import esprit.tn.flexifin.serviceInterfaces.IAccountService;
import lombok.AllArgsConstructor;
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

    public Account updateAccount(Account account) {
        return iAccountService.updateAccount(account);
    }

    public Account retrieveAccount(Long idAccount) {
        return iAccountService.retrieveAccount(idAccount);
    }

    IAccountService iAccountService;
@PostMapping("addAccount/{idP}")
    public Account addAccountForProfile(@PathVariable("idP") Long profileId, @RequestBody Account account) {
        return iAccountService.addAccountForProfile(profileId, account);
    }
}