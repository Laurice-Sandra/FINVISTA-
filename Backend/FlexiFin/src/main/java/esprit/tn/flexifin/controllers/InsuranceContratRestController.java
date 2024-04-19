package esprit.tn.flexifin.controllers;

import esprit.tn.flexifin.Dto.MailRequest;
import esprit.tn.flexifin.Dto.MailResponse;
import esprit.tn.flexifin.entities.InsuranceContrat;
import esprit.tn.flexifin.serviceImp.EmailServiceImpl;
import esprit.tn.flexifin.serviceInterfaces.IInsuranceContratService;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
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

   /* @PostMapping("/sendingEmail")
    public MailResponse sendEmail(@RequestBody MailRequest request) {
        Map<String, Object> model = new HashMap<>();
        model.put("Name", request.getName());
        model.put("location", "Ariana,Tunisia");
        return service.sendEmail(request, model);

    }*/
//   @GetMapping("/sendingEmail")
//    public void sendEmailWithFreemarkerTemplate(@RequestParam("to") String to, @RequestParam("subject")String subject, @RequestParam("templateName") String templateName) throws MessagingException, IOException, TemplateException, jakarta.mail.MessagingException {
//       Map<String, Object> templateModel = new HashMap<>();
//       templateModel.put("Name", "flora");
//       templateModel.put("location", "Ariana,Tunisia");
//        iInsuranceContratService.sendEmailWithFreemarkerTemplate(to, subject, templateModel, templateName);
//    }
}