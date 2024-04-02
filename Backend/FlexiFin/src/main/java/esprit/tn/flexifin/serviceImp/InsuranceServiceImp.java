package esprit.tn.flexifin.serviceImp;

import esprit.tn.flexifin.entities.InsuranceContrat;
import esprit.tn.flexifin.repositories.InsuranceRepository;
import esprit.tn.flexifin.serviceInterfaces.IInsuranceContratService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class InsuranceServiceImp implements IInsuranceContratService {
   InsuranceRepository insuranceRepository;
    private final JavaMailSender emailSender;
    private FreeMarkerConfigurer freemarkerConfigurer;

    @Override
    public List<InsuranceContrat> retrieveAllInsuranceContrats() {
        return null;
    }

    @Override
    public InsuranceContrat addInsuranceContrat(InsuranceContrat insuranceContrat) {
        return null;
    }

    @Override
    public InsuranceContrat updateInsuranceContrat(InsuranceContrat insuranceContrat) {
        return null;
    }

    @Override
    public InsuranceContrat retrieveInsuranceContrat(Long idContrat) {
        return null;
    }

   // @Override
//    public void sendEmailWithFreemarkerTemplate(String to, String subject, Map<String, Object> templateModel, String attachmentPath, String templateName) throws MessagingException, IOException, TemplateException, jakarta.mail.MessagingException, TemplateException {
//        MimeMessage mimeMessage = emailSender.createMimeMessage();
//        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
//        messageHelper.setFrom("noreply@finvistaflexifin.com");
//        messageHelper.setTo(to);
//        messageHelper.setSubject(subject);

        // Configuration de FreeMarker
//        Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate(templateName);
//        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
//        messageHelper.setText(htmlBody, true);

        // Ajout de la pièce jointe si nécessaire
//        if (attachmentPath != null && !attachmentPath.isEmpty()) {
//            FileSystemResource file = new FileSystemResource(attachmentPath);
//            messageHelper.addAttachment(file.getFilename(), file);
//        }

//        emailSender.send(mimeMessage);
    //}
}
