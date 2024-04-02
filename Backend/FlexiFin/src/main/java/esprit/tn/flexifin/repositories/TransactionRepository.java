package esprit.tn.flexifin.repositories;

import esprit.tn.flexifin.entities.TranStatus;
import esprit.tn.flexifin.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    //Filter
    @Query("SELECT t FROM Transaction t WHERE (:status IS NULL OR t.status = :status) AND (:date IS NULL OR t.date = :date)")
    List<Transaction> findByStatusAndDate(@Param("status") TranStatus status, @Param("date") Date date);

    // SumByStatus
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.status = :status")
    Integer sumTransactionsByStatus(@Param("status") TranStatus status);

    // SumByDate
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.date = :date")
    Integer sumTransactionsByDate(@Param("date") Date date);


    // TRANSACTION HISTORY
    @Query("SELECT t FROM Transaction t WHERE t.account.idAccount = :accountId")
    List<Transaction> findByAccountId(@Param("accountId") Long accountId);

    //Excel
    @Query("SELECT t FROM Transaction t WHERE (:accountIds IS NULL OR t.account.idAccount IN :accountIds) AND (:startDate IS NULL OR t.date >= :startDate) AND (:endDate IS NULL OR t.date <= :endDate)")
    List<Transaction> findByAccountIdsAndDateRange(@Param("accountIds") List<Long> accountIds, @Param("startDate") java.util.Date startDate, @Param("endDate") java.util.Date endDate);

}

