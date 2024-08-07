package esprit.tn.flexifin.repositories;

import esprit.tn.flexifin.entities.TypeContrat;
import esprit.tn.flexifin.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String username);
    @Query("Select u From User u join u.profileUser.insurances ass join ass.insuranceContrat c where c.type = :tc")
    Set<User> getByAssuranceType(@Param("tc") TypeContrat tc);
}