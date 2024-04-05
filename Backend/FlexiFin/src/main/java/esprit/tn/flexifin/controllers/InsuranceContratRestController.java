package esprit.tn.flexifin.controllers;

import esprit.tn.flexifin.Dto.MailRequest;
import esprit.tn.flexifin.Dto.MailResponse;
import esprit.tn.flexifin.entities.InsuranceContrat;
import esprit.tn.flexifin.serviceImp.EmailServiceImpl;
import esprit.tn.flexifin.serviceInterfaces.IInsuranceContratService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/insuranceC")
public class InsuranceContratRestController {
    private EmailServiceImpl service;
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

    @PostMapping("/sendingEmail")
    public MailResponse sendEmail(@RequestBody MailRequest request) {
        Map<String, Object> model = new HashMap<>();
        model.put("Name", request.getName());
        model.put("location", "Ariana,Tunisia");
        return service.sendEmail(request, model);

    }
}