package esprit.tn.flexifin.controllers;

import esprit.tn.flexifin.entities.Insurance;
import esprit.tn.flexifin.entities.InsuranceContrat;
import esprit.tn.flexifin.entities.TypeContrat;
import esprit.tn.flexifin.entities.User;
import esprit.tn.flexifin.serviceInterfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserRestController {
    IUserService iUserService;
    @GetMapping("/GetAllUser")
    public List<User> retrieveAllUsers() {
        return iUserService.retrieveAllUsers();
    }
    @PostMapping("/addUser")
    public User addUser(@RequestBody User user) {
        return iUserService.addUser(user);
    }
    @PutMapping("/updateUser")
    public User updateUser(@RequestBody User user) {
        return iUserService.updateUser(user);
    }
    @GetMapping("/addUser/{idUser}")
    public User retrieveUser(@PathVariable("idUser") Long idUser) {
        return iUserService.retrieveUser(idUser);
    }

    @PostMapping("/addInsuranceContrat")
        @ResponseBody
        public InsuranceContrat addContrat(@RequestBody InsuranceContrat ic){
            return iUserService.addInsuranceContrat(ic);
        }

        @PostMapping("/addInsurance/{cinU}/{idContrat}")
        @ResponseBody
        public Insurance addInsurance(@RequestBody Insurance insurance, @PathVariable("cinU") int cinU, @PathVariable("idContrat")String idContrat){
            return iUserService.addInsurance(insurance, cinU, Long.valueOf(idContrat));
        }


        @GetMapping("/getContratU/{id-User}")
        @ResponseBody
        public InsuranceContrat getContratByU(@PathVariable("id-User")Long idU){
            return iUserService.getContratU(idU);
        }
        @GetMapping("/getUserByType/{typeC}")
        public Set<User> getUserByType(@PathVariable("typeC") TypeContrat tc) {
            return iUserService.getUserByType(tc);
        }

    	@GetMapping("/getMontantU/{cin-User}")
    	public float getMontantU(@PathVariable("cin-User") int cinU){
    		return iUserService.getMontantU(cinU);
    	}

        //@PutMapping("/generatetestpdf")
    //    public String createLoanSimulationPdf(@RequestBody Loan loan) throws DocumentException, FileNotFoundException {
    //        return iLoanService.createLoanSimulationPdf(loan);
    //    }
}
