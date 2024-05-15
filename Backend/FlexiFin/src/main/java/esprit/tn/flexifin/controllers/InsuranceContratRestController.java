package esprit.tn.flexifin.controllers;

import esprit.tn.flexifin.Dto.MailRequest;
import esprit.tn.flexifin.Dto.MailResponse;
import esprit.tn.flexifin.entities.InsuranceContrat;
import esprit.tn.flexifin.entities.TypeContrat;
import esprit.tn.flexifin.serviceInterfaces.IInsuranceContratService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public InsuranceContrat addContrat(@RequestBody InsuranceContrat ic) {
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
    public InsuranceContrat getContratByU(@PathVariable("id-User") Long idU) {
        return iInsuranceContratService.getContratU(idU);
    }
    @DeleteMapping("/deleteContrat/{idContrat}")
    public void deleteContrat(@PathVariable("idContrat") Long idContrat) {
        iInsuranceContratService.deleteContrat(idContrat);
    }

    @GetMapping("/calculEndDate/{start-date}/{type-contrat}")
    public Date calculateEndDate(@PathVariable("start-date") Date startDate,@PathVariable("type-contrat") TypeContrat type) {
        return iInsuranceContratService.calculateEndDate(startDate, type);
    }
    @GetMapping("/retrievIC/{idContrat}")
    public InsuranceContrat retrieveInsuranceContrat(@PathVariable("idContrat") Long idContrat) {
        return iInsuranceContratService.retrieveInsuranceContrat(idContrat);
    }

}