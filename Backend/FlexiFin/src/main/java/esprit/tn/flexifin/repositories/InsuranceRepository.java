
package esprit.tn.flexifin.repositories;

import esprit.tn.flexifin.entities.Insurance;
import esprit.tn.flexifin.entities.TypeContrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
//    @Query("Select SUM(a.montant) From Insurance a where a.beneficiary.idBeneficiary = :id ")
//    float getMontantAnnuelByBf(@Param("id")int idBf);
//
//    @Query("Select SUM(a.montant) From Insurance a where a.insuranceContrat.type = :typeC")
//    float getSumByType(@Param("typeC") TypeContrat typeContrat);
}
