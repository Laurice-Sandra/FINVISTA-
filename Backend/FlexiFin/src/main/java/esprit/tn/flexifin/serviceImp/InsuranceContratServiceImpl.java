package esprit.tn.flexifin.serviceImp;

import esprit.tn.flexifin.entities.Insurance;
import esprit.tn.flexifin.entities.InsuranceContrat;
import esprit.tn.flexifin.entities.TypeContrat;
import esprit.tn.flexifin.entities.User;
import esprit.tn.flexifin.repositories.InsuranceContratRepository;
import esprit.tn.flexifin.repositories.InsuranceRepository;
import esprit.tn.flexifin.repositories.UserRepository;
import esprit.tn.flexifin.serviceInterfaces.IInsuranceContratService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class InsuranceContratServiceImpl implements IInsuranceContratService {
    InsuranceRepository insuranceRepository;
    InsuranceContratRepository insuranceContratRepository;
    UserRepository userRepository;
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
        return insuranceContratRepository.findById(idContrat).orElse(null);


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

        InsuranceContrat insuranceContrat = user.getProfileUser().getInsurances().get(0).getInsuranceContrat();
        LocalDate oldDate = insuranceContrat.getDateEffet();

        for (int i = 1; i < user.getProfileUser().getInsurances().size(); i++) {
            Insurance insurance = user.getProfileUser().getInsurances().get(i);
            if (oldDate.isAfter(insurance.getInsuranceContrat().getDateEffet())) {
                insuranceContrat = insurance.getInsuranceContrat();
            }
        }
        return insuranceContrat;
    }

}