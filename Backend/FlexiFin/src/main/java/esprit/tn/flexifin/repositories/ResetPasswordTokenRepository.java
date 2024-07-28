package esprit.tn.flexifin.repositories;


import esprit.tn.flexifin.entities.ResetPasswordToken;
import esprit.tn.flexifin.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Integer> {
    void removeAllByUser(User user) ;
    Optional<ResetPasswordToken> findByToken(String token) ;
}