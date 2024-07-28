package esprit.tn.flexifin.repositories;

import esprit.tn.flexifin.entities.Profile;
import esprit.tn.flexifin.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    //Profile findByUserId(Long userId);

}