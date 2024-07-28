package esprit.tn.flexifin.serviceImp;

import esprit.tn.flexifin.entities.Insurance;
import esprit.tn.flexifin.entities.InsuranceContrat;
import esprit.tn.flexifin.entities.Profile;
import esprit.tn.flexifin.repositories.InsuranceContratRepository;
import esprit.tn.flexifin.repositories.InsuranceRepository;
import esprit.tn.flexifin.repositories.ProfileRepository;
import esprit.tn.flexifin.repositories.UserRepository;
import esprit.tn.flexifin.serviceInterfaces.IInsuranceService;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.List;

@Service
@AllArgsConstructor
public class InsuranceServiceImp implements IInsuranceService {
    InsuranceRepository insuranceRepository;
    InsuranceContratRepository insuranceContratRepository;
    UserRepository userRepository;
    private final JavaMailSender emailSender;
    private FreeMarkerConfigurer freemarkerConfigurer;
    private final ProfileRepository profileRepository;

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
    public Insurance addInsurance(Insurance a, Long cinP, Long idContrat) {
        Profile profile = profileRepository.findById(cinP).orElse(null);
        InsuranceContrat insuranceContrat = insuranceContratRepository.findByIdContrat(idContrat);
        a.setProfile(profile);
        a.setInsuranceContrat(insuranceContrat);
        return insuranceRepository.save(a);
    }

}