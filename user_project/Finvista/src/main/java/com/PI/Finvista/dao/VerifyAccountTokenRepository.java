package com.PI.Finvista.dao;


import com.PI.Finvista.entities.ResetPasswordToken;
import com.PI.Finvista.entities.User;
import com.PI.Finvista.entities.VerifyAccountToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerifyAccountTokenRepository extends JpaRepository<VerifyAccountToken,Integer> {
    void removeAllByUser(User user) ;
    Optional<VerifyAccountToken> findByToken(String token) ;
}
