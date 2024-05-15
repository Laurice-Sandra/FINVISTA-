package esprit.tn.flexifin.serviceImp;

import esprit.tn.flexifin.entities.*;
import esprit.tn.flexifin.repositories.InsuranceContratRepository;
import esprit.tn.flexifin.repositories.InsuranceRepository;
import esprit.tn.flexifin.repositories.UserRepository;
import esprit.tn.flexifin.serviceInterfaces.IUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImp implements IUserService {
    UserRepository userRepository;
    InsuranceRepository insuranceRepository;
    InsuranceContratRepository insuranceContratRepository;


    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public User retrieveUser(Long idUser) {
        return userRepository.findById(idUser).orElse(null);
    }

    @Override
    public float getMontantU(int cinU) {
        User user = userRepository.getByCin(cinU);
        float montantInsuranceContrat = 0;
        for (Insurance ins : user.getInsurances()) {
            if (ins.getInsuranceContrat().getType() == TypeContrat.Monthly) {
                montantInsuranceContrat += ins.getMontant() * 12;
            } else if (ins.getInsuranceContrat().getType() == TypeContrat.Biannual) {
                montantInsuranceContrat += ins.getMontant() * 2;
            } else if (ins.getInsuranceContrat().getType() == TypeContrat.Annual){
                montantInsuranceContrat += ins.getMontant();
            }
        }
        return montantInsuranceContrat;
    }

    @Override
    public Set<User> getUserByType(TypeContrat tc) {
        return userRepository.getByAssuranceType(tc);
    }

    @Override
    @Scheduled(cron = "*/60 * * * * *")
    public void statistiques() {

        TreeMap<Integer, Integer> myStat = new TreeMap<>(Collections.reverseOrder());

        for (User u : userRepository.findAll()) {
            myStat.put(u.getInsurances().size(), u.getCin());
        }
        for (Map.Entry<Integer, Integer> va : myStat.entrySet()) {
            log.info(va.getKey() + "|" + va.getValue());
        }
    }



}