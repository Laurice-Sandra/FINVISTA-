package esprit.tn.flexifin.serviceInterfaces;

import esprit.tn.flexifin.entities.InsuranceContrat;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IInsuranceContratService {
    List<InsuranceContrat> retrieveAllInsuranceContrats();

    InsuranceContrat addInsuranceContrat(InsuranceContrat insuranceContrat);


    InsuranceContrat updateInsuranceContrat (InsuranceContrat insuranceContrat);

    InsuranceContrat retrieveInsuranceContrat (Long idContrat);

}

