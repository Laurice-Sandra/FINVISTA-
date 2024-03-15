package esprit.tn.flexifin.serviceImp;

import esprit.tn.flexifin.entities.InsuranceContrat;
import esprit.tn.flexifin.serviceInterfaces.IInsuranceContratService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class InsuranceServiceImp implements IInsuranceContratService {
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
}
