package esprit.tn.flexifin.repositories;

import esprit.tn.flexifin.entities.InsuranceContrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceContratRepository extends JpaRepository<InsuranceContratRepository,Long> {
    InsuranceContrat getByMatricule(String matricule);
}
