package esprit.tn.flexifin.controllers;

import esprit.tn.flexifin.entities.Insurance;
import esprit.tn.flexifin.entities.InsuranceContrat;
import esprit.tn.flexifin.serviceInterfaces.IInsuranceContratService;
import esprit.tn.flexifin.serviceInterfaces.IInsuranceService;
import esprit.tn.flexifin.serviceInterfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/insurance")
@CrossOrigin("*")
public class InsuranceRestController {
    IInsuranceService iInsuranceService;
    @PostMapping("/addInsurance/{cinU}/{idContrat}")
    @ResponseBody
    public Insurance addInsurance(@RequestBody Insurance insurance, @PathVariable("cinU") int cinU, @PathVariable("idContrat")String idContrat){
        return iInsuranceService.addInsurance(insurance, cinU, Long.valueOf(idContrat));
    }
    @GetMapping("/GetAllInsurance")
    public List<Insurance> retrieveAllInsurances() {
        return iInsuranceService.retrieveAllInsurances();
    }
    @PostMapping("/addInsurance")
    public Insurance addInsurance(@RequestBody Insurance i) {
        return iInsuranceService.addInsurance(i);
    }
    @DeleteMapping("/delete/{idInsurance}")
    public void delete(@PathVariable("idInsurance") Long idInsurance) {
        iInsuranceService.delete(idInsurance);
    }
    @PutMapping("/update")
    public Insurance updateInsurance(@RequestBody Insurance i) {
        return iInsuranceService.updateInsurance(i);
    }
    @GetMapping("/getInsurance/{idUser}")
    public Insurance retrieveInsurance(Long idUser) {
        return iInsuranceService.retrieveInsurance(idUser);
    }
}
