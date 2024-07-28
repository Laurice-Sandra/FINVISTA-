package esprit.tn.flexifin.serviceImp;


import esprit.tn.flexifin.dto.requests.MailRequest;
import esprit.tn.flexifin.dto.responses.MailResponse;
import esprit.tn.flexifin.serviceInterfaces.IEmailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
@Service
@AllArgsConstructor
public class EmailServiceImpl implements IEmailService {
    private JavaMailSender sender;
    private JavaMailSender javaMailSender;
    private Configuration config;
    @Override
    public MailResponse sendEmail(MailRequest request, Map<String, Object> model) {
        MailResponse response = new MailResponse();
        MimeMessage message = sender.createMimeMessage();
        try {
            // set mediaType
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            // add attachment
            helper.addAttachment("logoflex.jpg", new ClassPathResource("logoflex.jpg"));

            Template t = config.getTemplate("email-template1.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            helper.setTo(request.getTo());
            helper.setText(html, true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom());
            sender.send(message);

            response.setMessage("mail send to : " + request.getTo());
            response.setStatus(Boolean.TRUE);

        } catch (IOException | TemplateException | jakarta.mail.MessagingException e) {
            response.setMessage("Mail Sending failure : "+e.getMessage());
            response.setStatus(Boolean.FALSE);
        }

        return response;
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(toEmail);
            messageHelper.setSubject("Password Reset Request");
            messageHelper.setText("Your password reset code is: " + resetToken);
        };

        javaMailSender.send(messagePreparator);
    }

}
