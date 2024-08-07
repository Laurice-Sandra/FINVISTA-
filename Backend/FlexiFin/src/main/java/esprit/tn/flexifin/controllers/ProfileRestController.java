package esprit.tn.flexifin.controllers;

import esprit.tn.flexifin.entities.Profile;
import esprit.tn.flexifin.serviceInterfaces.IProfileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/profile")
public class ProfileRestController {
    IProfileService iProfileService;
    @GetMapping("/GetAllProfile")
    public List<Profile> retrieveAllProfiles() {
        return iProfileService.retrieveAllProfiles();
    }
    @PostMapping("/addProfile/{idU}")

    public Profile addProfileforUser(@RequestBody Profile profile,@PathVariable("idU") Long idUser) {
        return iProfileService.addProfileAssignUser(profile,idUser);
    }
    @PutMapping ("/updateProfile")
    public Profile updateProfile(@RequestBody Profile profile) {
        return iProfileService.updateProfile(profile);
    }
    @GetMapping("/getprofile/{idProfile}")
    public Profile retrieveProfile(@PathVariable("idProfile") Long idProfile) {
        return iProfileService.retrieveProfile(idProfile);
    }




}
