package esprit.tn.flexifin.controllers;

import esprit.tn.flexifin.entities.InsuranceContrat;
import esprit.tn.flexifin.serviceInterfaces.IInsuranceContratService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/insurance")
public class InsuranceContratRestController {
     IInsuranceContratService iInsuranceContratService;

        @GetMapping("/GetAllInsuranceContrat")
        public List<InsuranceContrat> retrieveAllInsuranceContrats() {
            return iInsuranceContratService.retrieveAllInsuranceContrats();
        }
        @PostMapping("/addInsuranceContrat")
        public InsuranceContrat addInsuranceContrat(@RequestBody InsuranceContrat insuranceContrat) {
            return iInsuranceContratService.addInsuranceContrat(insuranceContrat);
        }
        @PutMapping ("/updateInsuranceContrat")
        public InsuranceContrat updateProfile(@RequestBody InsuranceContrat insuranceContrat) {
            return iInsuranceContratService.updateInsuranceContrat(insuranceContrat);
        }
        @GetMapping("/getContrat/{idContrat}")
        public InsuranceContrat retrieveProfile(@PathVariable("idContrat") Long idContrat) {
            return iInsuranceContratService.retrieveInsuranceContrat(idContrat);
        }
}