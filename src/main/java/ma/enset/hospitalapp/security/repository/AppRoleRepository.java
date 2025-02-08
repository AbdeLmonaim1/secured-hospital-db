package ma.enset.hospitalapp.security.repository;

import ma.enset.hospitalapp.security.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole, String> {

}
