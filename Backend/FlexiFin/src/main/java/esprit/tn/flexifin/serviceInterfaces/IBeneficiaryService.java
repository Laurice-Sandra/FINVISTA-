package esprit.tn.flexifin.serviceInterfaces;

import esprit.tn.flexifin.entities.Beneficiary;
import esprit.tn.flexifin.entities.Insurance;
import esprit.tn.flexifin.entities.InsuranceContrat;
import esprit.tn.flexifin.entities.TypeContrat;

import java.util.Set;

public interface IBeneficiaryService {
    Beneficiary addBeneficiary(Beneficiary bf);

    InsuranceContrat addInsuranceContrat (InsuranceContrat c) ;

    Insurance addInsurance (Insurance a, int cinBf, String matricule);

    InsuranceContrat getContratBf(int idBf);

    float getMontantBf(int cinBf);

    Set<Beneficiary> getBeneficaryByType(TypeContrat tc);

    void statistiques();
}
