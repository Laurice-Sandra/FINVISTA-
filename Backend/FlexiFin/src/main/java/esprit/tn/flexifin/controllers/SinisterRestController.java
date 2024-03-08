package esprit.tn.flexifin.controllers;

import esprit.tn.flexifin.entities.Sinister;
import esprit.tn.flexifin.serviceInterfaces.ISinisterService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/sinister")
public class SinisterRestController {
    ISinisterService iSinisterService;
    @GetMapping("/GetAllSinister")
    public List<Sinister> retrieveAllProfiles() {
        return iSinisterService.retrieveAllSinisters();
    }
    @PostMapping("/addSinister")
    public Sinister addSinister(@RequestBody Sinister sinister) {
        return iSinisterService.addSinister(sinister);
    }
    @PutMapping ("/updateSinister")
    public Sinister updateSinister(@RequestBody Sinister sinister) {
        return iSinisterService.updateSinister(sinister);
    }
    @GetMapping("/getsinister/{idSinister}")
    public Sinister retrieveProfile(@PathVariable("idSinister") Long idSinister) {
        return iSinisterService.retrieveSinister(idSinister);
    }

}
