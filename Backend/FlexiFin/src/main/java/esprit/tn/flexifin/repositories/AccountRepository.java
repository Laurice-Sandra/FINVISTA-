package esprit.tn.flexifin.repositories;

import esprit.tn.flexifin.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountRepository extends JpaRepository<Account, Long> {
}
