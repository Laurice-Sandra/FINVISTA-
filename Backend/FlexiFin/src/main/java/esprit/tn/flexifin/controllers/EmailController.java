package esprit.tn.flexifin.controllers;


import esprit.tn.flexifin.dto.requests.MailRequest;
import esprit.tn.flexifin.dto.responses.MailResponse;
import esprit.tn.flexifin.serviceImp.EmailServiceImpl;
import esprit.tn.flexifin.serviceInterfaces.IEmailService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
@AllArgsConstructor
@RequestMapping("/Email")
public class EmailController {
    private IEmailService iEmailService;

    @PostMapping("/sendingEmail")
    public MailResponse sendEmail(@RequestBody MailRequest request) {
        Map<String, Object> model = new HashMap<>();
        model.put("Name", request.getName());
        model.put("location", "Ariana,Tunisia");
        return iEmailService.sendEmail(request, model);

    }
}