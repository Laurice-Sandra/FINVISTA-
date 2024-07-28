package com.PI.Finvista.dao;


import com.PI.Finvista.entities.ResetPasswordToken;
import com.PI.Finvista.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken,Integer> {

    void removeAllByUser(User user) ;
    Optional<ResetPasswordToken> findByToken(String token) ;

}
