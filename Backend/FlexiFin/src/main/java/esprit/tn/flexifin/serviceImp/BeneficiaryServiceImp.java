package esprit.tn.flexifin.serviceImp;

import esprit.tn.flexifin.entities.Beneficiary;
import esprit.tn.flexifin.entities.Insurance;
import esprit.tn.flexifin.entities.InsuranceContrat;
import esprit.tn.flexifin.entities.TypeContrat;
import esprit.tn.flexifin.repositories.BeneficiaryRepository;
import esprit.tn.flexifin.repositories.InsuranceContratRepository;
import esprit.tn.flexifin.repositories.InsuranceRepository;
import esprit.tn.flexifin.serviceInterfaces.IBeneficiaryService;


import java.util.Date;
import java.util.Set;

public class BeneficiaryServiceImp implements IBeneficiaryService {

    BeneficiaryRepository beneficiaryRepository;
    InsuranceRepository insuranceRepository;
    InsuranceContratRepository insuranceContratRepository;
    @Override
    public Beneficiary addBeneficiary(Beneficiary bf) {
        return beneficiaryRepository.save(bf);
    }
    @Override
    public InsuranceContrat addInsuranceContrat(InsuranceContrat ic) {
        return insuranceContratRepository.save(ic)
    }
    @Override
    public Insurance addInsurance(Insurance a, int cinBf, String matricule) {
        Beneficiary beneficiary = beneficiaryRepository.getByCin(cinBf);
        InsuranceContrat insuranceContrat = insuranceContratRepository.getByMatricule(matricule);
        a.setBeneficiary(beneficiary);
        a.setInsuranceContrat(insuranceContrat);
        return insuranceRepository.save(a);
    }

    @Override
    public InsuranceContrat getContratBf(Long idBf) {
        Beneficiary beneficiary = beneficiaryRepository.findById(idBf).orElse(null);

        InsuranceContrat insuranceContrat = beneficiary.getInsurances().get(0).getInsuranceContrat();
        Date oldDate = InsuranceContrat.getDateEffet();

        for (int i = 1; i < beneficiary.getInsurances().size(); i++) {
            Insurance insurance = beneficiary.getInsurances().get(i);
            if (oldDate.after(insurance.getInsuranceContrat().getDateEffet())) {
                insuranceContrat = insurance.getInsuranceContrat();
            }
        }
        return insuranceContrat;
    }

    @Override
    public float getMontantBf(int cinBf) {
        Beneficiary beneficiary = beneficiaryRepository.getByCin(cinBf);
        float montantInsuranceContrat = 0;
        for (Insurance ins : beneficiary.getInsurances()) {
            if (ins.getInsuranceContrat().getType() == TypeContrat.Mensuel) {
                montantInsuranceContrat += ins.getMontant() * 12;
            } else if (ins.getInsuranceContrat().getType() == TypeContrat.Semestriel) {
                montantInsuranceContrat += ins.getMontant() * 2;
            } else {
                montantInsuranceContrat += ins.getMontant();
            }
        }
    }

    @Override
    public Set<Beneficiary> getBeneficaryByType(TypeContrat tc) {
        return beneficiaryRepository.getByAssuranceType(tc);
    }

    @Override
    public void statistiques() {

    }
}
