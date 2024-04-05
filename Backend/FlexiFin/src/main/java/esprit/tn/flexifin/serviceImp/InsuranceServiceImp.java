package esprit.tn.flexifin.serviceImp;

import esprit.tn.flexifin.entities.Insurance;
import esprit.tn.flexifin.entities.InsuranceContrat;
import esprit.tn.flexifin.entities.User;
import esprit.tn.flexifin.repositories.InsuranceContratRepository;
import esprit.tn.flexifin.repositories.InsuranceRepository;
import esprit.tn.flexifin.repositories.UserRepository;
import esprit.tn.flexifin.serviceInterfaces.IAccountService;
import esprit.tn.flexifin.serviceInterfaces.IInsuranceContratService;
import esprit.tn.flexifin.serviceInterfaces.IInsuranceService;
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
public class InsuranceServiceImp implements IInsuranceService {
   InsuranceRepository insuranceRepository;
   InsuranceContratRepository insuranceContratRepository;
   UserRepository userRepository;


    @Override
    public List<Insurance> retrieveAllInsurances() {
        return insuranceRepository.findAll();
    }

    @Override
    public Insurance addInsurance(Insurance i) {
        return insuranceRepository.save(i);
    }

    @Override
    public Insurance updateInsurance(Insurance i) {
        return insuranceRepository.save(i);
    }

    @Override
    public Insurance retrieveInsurance(Long idAssurance) {
        return insuranceRepository.findById(idAssurance).orElse(null);
    }
    @Override
    public Insurance addInsurance(Insurance a, int cinU, Long idContrat) {
        User user = userRepository.getByCin(cinU);
        InsuranceContrat insuranceContrat = insuranceContratRepository.findByIdContrat(idContrat);
        a.setUser(user);
        a.setInsuranceContrat(insuranceContrat);
        return insuranceRepository.save(a);
    }


}
