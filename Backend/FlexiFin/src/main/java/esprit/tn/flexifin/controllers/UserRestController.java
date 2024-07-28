package esprit.tn.flexifin.controllers;

import esprit.tn.flexifin.dto.requests.UpdateUserRequest;
import esprit.tn.flexifin.dto.responses.ImageResponse;
import esprit.tn.flexifin.dto.responses.MessageResponse;
import esprit.tn.flexifin.entities.TypeContrat;
import esprit.tn.flexifin.entities.User;
import esprit.tn.flexifin.serviceInterfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
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




    //insurance controller

    //insuranceContrat controller

    @GetMapping("/getUserByType/{typeC}")
    public Set<User> getUserByType(@PathVariable("typeC") TypeContrat tc) {
        return iUserService.getUserByType(tc);
    }

    @GetMapping("/getMontantU/{cin-User}")
    public float getMontantU(@PathVariable("cin-User") Long cinU){
        return iUserService.getMontantU(cinU);
    }

    //@PutMapping("/generatetestpdf")
    //    public String createLoanSimulationPdf(@RequestBody Loan loan) throws DocumentException, FileNotFoundException {
    //        return iLoanService.createLoanSimulationPdf(loan);
    //    }


    @GetMapping("/current")
    public ResponseEntity<?> getCurrent(Principal connectedUser) {
        final User responseBody ;
        try {
            responseBody =  iUserService.getCurrentUser(connectedUser);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok().body(responseBody) ;
    }

    @PutMapping("/current")
    public ResponseEntity<?> updateCurrent(Principal connectedUser, @RequestBody UpdateUserRequest updatedUser) {
        try {
            iUserService.updateCurrentUser(connectedUser,updatedUser);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok().body("User updated successfully !") ;
    }

    @DeleteMapping("/current")
    public ResponseEntity<?> deleteCurrent(Principal connectedUser) {
        try {
            iUserService.deleteCurrentUser(connectedUser);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok().body("User deleted successfully !") ;
    }

    @PostMapping("/current/image")
    public ResponseEntity<?> addProfileImage(@RequestBody MultipartFile imageFile, Principal connectedUser)   {
        try {
            iUserService.addProfileImage(imageFile,connectedUser);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok().body("profile image added successfully !") ;
    }

    @GetMapping("/current/image")
    public ResponseEntity<?> getProfileImage( Principal connectedUser) {
        byte[] responseBody ;
        try {
            responseBody =  iUserService.getProfileImage(connectedUser) ;
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }


        return ResponseEntity.ok().body(new ImageResponse(responseBody)) ;
    }

    @PutMapping("/current/image")
    public ResponseEntity<?> updateProfileImage(@RequestBody  MultipartFile imageFile, Principal connectedUser)   {
        try {
            iUserService.updateProfileImage(imageFile,connectedUser);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage())) ;
        }
        return ResponseEntity.ok().body("profile image updated successfully !") ;
    }







}
