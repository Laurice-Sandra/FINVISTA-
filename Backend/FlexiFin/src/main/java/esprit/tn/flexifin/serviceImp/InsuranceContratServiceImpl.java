package esprit.tn.flexifin.serviceImp;

import esprit.tn.flexifin.entities.Insurance;
import esprit.tn.flexifin.entities.InsuranceContrat;
import esprit.tn.flexifin.entities.TypeContrat;
import esprit.tn.flexifin.entities.User;
import esprit.tn.flexifin.repositories.InsuranceContratRepository;
import esprit.tn.flexifin.repositories.InsuranceRepository;
import esprit.tn.flexifin.repositories.UserRepository;
import esprit.tn.flexifin.serviceInterfaces.IInsuranceContratService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class InsuranceContratServiceImpl implements IInsuranceContratService {
    InsuranceRepository insuranceRepository;
    InsuranceContratRepository insuranceContratRepository;
    UserRepository userRepository;
    private JavaMailSender emailSender;
    private FreeMarkerConfigurer freemarkerConfigurer;
    @Override
    public List<InsuranceContrat> retrieveAllInsuranceContrats() {
       return insuranceContratRepository.findAll();
    }

    @Override
    public InsuranceContrat updateInsuranceContrat(InsuranceContrat insuranceContrat) {
        return insuranceContratRepository.save(insuranceContrat);
    }

    @Override
    public InsuranceContrat retrieveInsuranceContrat(Long idContrat) {
        return insuranceRepository.findById(idContrat).orElse(null).getInsuranceContrat();
    }
    @Override
    public InsuranceContrat addInsuranceContrat(InsuranceContrat c) {
        // Calculate end date based on contract type
        c.setEndDate(calculateEndDate(c.getStartDate(), c.getType()));

        // Save the insurance contract
        return insuranceContratRepository.save(c);
    }



    public Date calculateEndDate(Date startDate, TypeContrat type) {
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        switch (type) {
            case Monthly:
                return Date.from(startLocalDate.plusMonths(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            case Biannual:
                return Date.from(startLocalDate.plusMonths(6).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            case Annual:
                return Date.from(startLocalDate.plusYears(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            default:
                return null; // Handle unknown contract types
        }
    }

    @Override
    public InsuranceContrat getContratU(Long idU) {
        User user = userRepository.findById(idU).orElse(null);

        InsuranceContrat insuranceContrat = user.getInsurances().get(0).getInsuranceContrat();
        LocalDate oldDate = insuranceContrat.getDateEffet();

        for (int i = 1; i < user.getInsurances().size(); i++) {
            Insurance insurance = user.getInsurances().get(i);
            if (oldDate.isAfter(insurance.getInsuranceContrat().getDateEffet())) {
                insuranceContrat = insurance.getInsuranceContrat();
            }
        }
        return insuranceContrat;
    }

    @Override
    public void sendEmailWithFreemarkerTemplate(String to, String subject, Map<String, Object> templateModel, String attachmentPath, String templateName) throws MessagingException, IOException, TemplateException, jakarta.mail.MessagingException, TemplateException {

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom("noreply@finvistaflexifin.com");
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);

        //Configuration de FreeMarker
        Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate(templateName);
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
        messageHelper.setText(htmlBody, true);

        //Ajout de la pièce jointe si nécessaire
        if (attachmentPath != null && !attachmentPath.isEmpty()) {
            FileSystemResource file = new FileSystemResource(attachmentPath);
            messageHelper.addAttachment(file.getFilename(), file);
        }

        emailSender.send(mimeMessage);
    }

}
