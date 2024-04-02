package esprit.tn.flexifin.serviceInterfaces;

import esprit.tn.flexifin.entities.InsuranceContrat;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IInsuranceContratService {
    List<InsuranceContrat> retrieveAllInsuranceContrats();

    InsuranceContrat addInsuranceContrat(InsuranceContrat insuranceContrat);


    InsuranceContrat updateInsuranceContrat (InsuranceContrat insuranceContrat);

    InsuranceContrat retrieveInsuranceContrat (Long idContrat);

  //  void sendEmailWithFreemarkerTemplate(String to, String subject, Map<String, Object> templateModel, String attachmentPath, String templateName) throws MessagingException, IOException, TemplateException, jakarta.mail.MessagingException;

}


