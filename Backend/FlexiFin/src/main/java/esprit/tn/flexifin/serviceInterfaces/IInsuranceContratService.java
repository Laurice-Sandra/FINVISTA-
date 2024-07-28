package esprit.tn.flexifin.serviceInterfaces;

import esprit.tn.flexifin.entities.InsuranceContrat;
import esprit.tn.flexifin.entities.TypeContrat;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IInsuranceContratService {
    List<InsuranceContrat> retrieveAllInsuranceContrats();
    Date calculateEndDate(Date startDate, TypeContrat type);
    InsuranceContrat addInsuranceContrat(InsuranceContrat c);

    InsuranceContrat updateInsuranceContrat (InsuranceContrat insuranceContrat);

    InsuranceContrat retrieveInsuranceContrat (Long idContrat);
    InsuranceContrat getContratU(Long idU);

    // void sendEmailWithFreemarkerTemplate(String to, String subject, Map<String, Object> templateModel, String templateName) throws MessagingException, IOException, TemplateException, jakarta.mail.MessagingException;

}
