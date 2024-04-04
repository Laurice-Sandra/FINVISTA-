package esprit.tn.flexifin.serviceInterfaces;

import esprit.tn.flexifin.entities.Insurance;
import esprit.tn.flexifin.entities.InsuranceContrat;

import java.util.List;

public interface IInsuranceService {
     List<Insurance> retrieveAllInsurances();
     Insurance addInsurance(Insurance i);
     Insurance updateInsurance(Insurance i);

     Insurance retrieveInsurance(Long idAssurance);

    Insurance addInsurance(Insurance a, int cinU, Long idContrat);
}
