package esprit.tn.flexifin.repositories;

import esprit.tn.flexifin.entities.Loan;
import esprit.tn.flexifin.entities.LoanStatus;
import esprit.tn.flexifin.entities.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    // Filtrer les prêts par statut
    List<Loan> findByLoanStatus(LoanStatus loanStatus);

    // Filtrer les prêts par date
    List<Loan> findByStartDate(LocalDate startDate);

    // Filtrer les prêts par type de prêt
    List<Loan> findByLoantype(LoanType loantype);

    List<Loan> findAllByLoanStatusAndNextPaymentDueDateIsBefore(LoanStatus loanStatus, LocalDate localDate);

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.loanStatus = 'InProgress' AND l.account.profile.idProfile = :profileId")
    Integer getTotalLoansByProfileId(@Param("profileId") Long profileId);

    @Query("SELECT l FROM Loan l JOIN l.account c JOIN c.profile p JOIN p.user u WHERE u.idUser = :userId")
    List<Loan> findByUserId(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(l.ammountRequest), 0) FROM Loan l WHERE l.loanStatus = 'Repaid' AND l.account.profile.idProfile = :profileId")
    Float getTotalAmountRepaidByProfileId(@Param("profileId") Long profileId);

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.loanStatus = 'Repaid' AND l.account.profile.idProfile= :profileId")
    Integer getTotalClosedLoansByProfileId(@Param("profileId") Long profileId);

    @Query("SELECT COALESCE(SUM(l.ammountRequest), 0) FROM Loan l WHERE   l.account.profile.idProfile = :profileId")
    Float getTotalAmountBorrowedByProfileId(@Param("profileId") Long profileId);







}