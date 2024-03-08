package esprit.tn.flexifin.repositories;

import esprit.tn.flexifin.entities.Beneficiary;
import esprit.tn.flexifin.entities.TypeContrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    Beneficiary getByCin(int cinBenef);
    @Query("Select b From Beneficiary b join b.insurances ass join ass.insuranceContrat c where c.type = :tc")
    Set<Beneficiary> getByAssuranceType(@Param("tc") TypeContrat tc);
}
