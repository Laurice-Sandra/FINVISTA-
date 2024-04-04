package esprit.tn.flexifin.controllers;

import esprit.tn.flexifin.entities.InsuranceContrat;
import esprit.tn.flexifin.serviceInterfaces.IInsuranceContratService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/insuranceC")
public class InsuranceContratRestController {

    IInsuranceContratService iInsuranceContratService;

    @GetMapping("/GetAllInsuranceContrat")
    public List<InsuranceContrat> retrieveAllInsuranceContrats() {
        return iInsuranceContratService.retrieveAllInsuranceContrats();
    }
    @PostMapping("/addInsuranceContrat")
    @ResponseBody
    public InsuranceContrat addContrat(@RequestBody InsuranceContrat ic){
        return iInsuranceContratService.addInsuranceContrat(ic);
    }

    @PutMapping("/updateInsuranceContrat")
    public InsuranceContrat updateProfile(@RequestBody InsuranceContrat insuranceContrat) {
        return iInsuranceContratService.updateInsuranceContrat(insuranceContrat);
    }

    @GetMapping("/getContrat/{idContrat}")
    public InsuranceContrat retrieveProfile(@PathVariable("idContrat") Long idContrat) {
        return iInsuranceContratService.retrieveInsuranceContrat(idContrat);
        }

    @GetMapping("/getContratU/{id-User}")
    @ResponseBody
    public InsuranceContrat getContratByU(@PathVariable("id-User")Long idU){
        return iInsuranceContratService.getContratU(idU);
    }
}