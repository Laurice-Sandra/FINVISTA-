package esprit.tn.flexifin.repositories;

import esprit.tn.flexifin.entities.AppSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppSettingRepository extends JpaRepository<AppSetting, Long> {
    Optional<AppSetting> findFirstByOrderByIdAsc();
}
