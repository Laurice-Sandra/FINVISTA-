package esprit.tn.flexifin.serviceInterfaces;

import esprit.tn.flexifin.Dto.MailRequest;
import esprit.tn.flexifin.Dto.MailResponse;

import java.util.Map;

public interface IEmailService {
    MailResponse sendEmail(MailRequest request, Map<String, Object> model);
    void sendPasswordResetEmail(String toEmail, String resetToken);
}