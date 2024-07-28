package esprit.tn.flexifin.repositories;

import esprit.tn.flexifin.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}