package esprit.tn.flexifin.repositories;

import esprit.tn.flexifin.entities.Sinister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SinisterRepository extends JpaRepository<Sinister, Long> {
}