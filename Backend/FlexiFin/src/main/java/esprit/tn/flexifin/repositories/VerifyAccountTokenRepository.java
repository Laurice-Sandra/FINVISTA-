package esprit.tn.flexifin.repositories;

import esprit.tn.flexifin.entities.User;
import esprit.tn.flexifin.entities.VerifyAccountToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerifyAccountTokenRepository extends JpaRepository<VerifyAccountToken, Integer> {
    void removeAllByUser(User user) ;
    Optional<VerifyAccountToken> findByToken(String token) ;
}