package esprit.tn.flexifin.controllers;

import esprit.tn.flexifin.entities.Beneficiary;
import esprit.tn.flexifin.entities.Insurance;
import esprit.tn.flexifin.entities.InsuranceContrat;
import esprit.tn.flexifin.entities.TypeContrat;
import esprit.tn.flexifin.serviceInterfaces.IBeneficiaryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/Beneficiary")
public class BeneficiaryRestController {
    IBeneficiaryService iBeneficiaryService;
    @PostMapping("/addBenef")
    @ResponseBody
    public Beneficiary addBeneficiary(@RequestBody Beneficiary bf){
        return iBeneficiaryService.addBeneficiary(bf);
    }
    @GetMapping("/GetAllBeneficiary")
    public List<Beneficiary> retrieveAllBeneficairy() {
        return iBeneficiaryService.retrieveAllBeneficiary();
    }

    @PostMapping("/addInsuranceContrat")
    @ResponseBody
    public InsuranceContrat addContrat(@RequestBody InsuranceContrat ic){
        return iBeneficiaryService.addInsuranceContrat(ic);
    }

//    @PostMapping("/addInsurance/{cinBf}/{matricule}")
//    @ResponseBody
//    public Insurance addInsurance(@RequestBody Insurance insurance, @PathVariable("cinBf") int cinBf, @PathVariable("matricule")String matricule){
//        return iBeneficiaryService.addInsurance(insurance, cinBf, matricule);
//    }

    //    @GetMapping("/getContratBf/{id-benef}")
//    @ResponseBody
//    public InsuranceContrat getContratByBf(@PathVariable("id-benef")Long idBf){
//        return iBeneficiaryService.getContratBf(idBf);
//    }
    @GetMapping("/getBeneficairesByType/{typeC}")
    public Set<Beneficiary> getBeneficairesByType(@PathVariable("typeC") TypeContrat tc) {
        return iBeneficiaryService.getBeneficaryByType(tc);
    }

}
