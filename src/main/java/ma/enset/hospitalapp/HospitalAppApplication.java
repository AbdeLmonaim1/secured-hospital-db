package ma.enset.hospitalapp;

import ma.enset.hospitalapp.entities.Patient;
import ma.enset.hospitalapp.repository.PatientRepository;
import ma.enset.hospitalapp.security.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.Date;

@SpringBootApplication
public class HospitalAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(HospitalAppApplication.class, args);
    }

//    @Bean
//    CommandLineRunner start(PatientRepository patientRepository){
//        return args -> {
//            patientRepository.save(new Patient(null,"Mohamed",new Date(),false,42));
//            patientRepository.save(new Patient(null,"Imane",new Date(),true,98));
//            patientRepository.save(new Patient(null,"Yassine",new Date(),true,342));
//            patientRepository.save(new Patient(null,"Laila",new Date(),false,123));
//        };
//    }
    //@Bean
    CommandLineRunner commandLineRunner(JdbcUserDetailsManager jdbcUserDetailsManager) {
        PasswordEncoder passwordEncoder = passwordEncoder();
        return args -> {
            UserDetails u1 = jdbcUserDetailsManager.loadUserByUsername("Ahmed");
            if(u1 == null)
                jdbcUserDetailsManager.createUser(
                    User.withUsername("Ahmed").password(passwordEncoder.encode("11111")).roles("USER").build()

            );
            UserDetails u2 = jdbcUserDetailsManager.loadUserByUsername("Ismail");
            if(u2 == null)
                jdbcUserDetailsManager.createUser(
                    User.withUsername("Ismail").password(passwordEncoder.encode("22222")).roles("USER").build()

            );
            UserDetails u3 = jdbcUserDetailsManager.loadUserByUsername("Tchicko");
            if(u3 == null)
                jdbcUserDetailsManager.createUser(
                    User.withUsername("Tchicko").password(passwordEncoder.encode("monaim11")).roles("USER","ADMIN").build()

            );
        };
    }
    //C'est pour teste la 3-ieme strategie UserDetailsService
    //@Bean
    CommandLineRunner commandLineRunnerUserDetailsService(AccountService accountService) {
        return args -> {
            accountService.addNewRole("USER");
            accountService.addNewRole("ADMIN");
            accountService.addNewUser("amine", "11111","amine@gmail.com","11111");
            accountService.addNewUser("mouad", "22222","mouad@gmail.com","22222");
            accountService.addNewUser("Abdelmonaim", "33333","abdelmonaim@gmail.com","33333");
            accountService.addRoleToUser("amine", "USER");
            accountService.addRoleToUser("mouad", "USER");
            accountService.addRoleToUser("Abdelmonaim", "USER");
            accountService.addRoleToUser("Abdelmonaim", "ADMIN");
        };
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
