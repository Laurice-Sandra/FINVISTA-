package esprit.tn.flexifin.controllers;

import esprit.tn.flexifin.entities.Insurance;
import esprit.tn.flexifin.serviceInterfaces.IInsuranceContratService;
import esprit.tn.flexifin.serviceInterfaces.IInsuranceService;
import esprit.tn.flexifin.serviceInterfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/insurance")
public class InsuranceRestController {
    IInsuranceService iInsuranceService;
    @PostMapping("/addInsurance/{cinU}/{idContrat}")
    @ResponseBody
    public Insurance addInsurance(@RequestBody Insurance insurance, @PathVariable("cinU") int cinU, @PathVariable("idContrat")String idContrat){
        return iInsuranceService.addInsurance(insurance, cinU, Long.valueOf(idContrat));
    }

}
