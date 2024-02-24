package esprit.tn.flexifin.controllers;

import esprit.tn.flexifin.entities.User;
import esprit.tn.flexifin.serviceInterfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
