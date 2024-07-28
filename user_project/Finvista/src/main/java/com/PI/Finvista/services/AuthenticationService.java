package com.PI.Finvista.services;


import com.PI.Finvista.dto.requests.AuthenticationRequest;
import com.PI.Finvista.dto.requests.ForgetPassword;
import com.PI.Finvista.dto.requests.RegisterRequest;
import com.PI.Finvista.dto.responses.AuthenticationResponse;
import com.PI.Finvista.entities.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public interface AuthenticationService {

    public void register(RegisterRequest request) throws MessagingException;
    public AuthenticationResponse authenticate(AuthenticationRequest request) ;
    public AuthenticationResponse refreshToken(HttpServletRequest request, String refreshToken) throws IOException ;

    public void logout() ;

    public void forgetPassword(ForgetPassword request) throws MessagingException;

    public String createResetPasswordToken(User concernedUser ) ;

    public String createVerifyAccountToken(User concernedUser ) ;

    public Object verifyResetPasswordToken(String token) ;

    public void resetPassword(String newPassword, String token) ;

    public void forgetPassword1(ForgetPassword request) ;

    public void sendVerifyAccountEmail(String email) throws  MessagingException;

    public void verifyAccount(String token) ;
}
