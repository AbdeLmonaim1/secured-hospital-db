package ma.enset.hospitalapp.security.service;

import ma.enset.hospitalapp.security.entities.AppRole;
import ma.enset.hospitalapp.security.entities.AppUser;
import ma.enset.hospitalapp.security.repository.AppRoleRepository;
import ma.enset.hospitalapp.security.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private AppRoleRepository roleRepository;
    private AppUserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    //Dependancies Injection
    public AccountServiceImpl(AppRoleRepository roleRepository, AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser addNewUser(String username, String password, String email, String confirmPassword) {
        AppUser user = userRepository.findByUsername(username);
        if (user != null) throw new RuntimeException("User already exists");
        if (!password.equals(confirmPassword)) throw new RuntimeException("Passwords do not match");
        AppUser appUser = AppUser.builder()
                .userId(UUID.randomUUID().toString())
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();
        AppUser savedUser = userRepository.save(appUser);
        return savedUser;
    }

    @Override
    public AppRole addNewRole(String role) {
        AppRole appRole = roleRepository.findById(role).orElse(null);
        if (appRole != null) throw new RuntimeException("Role already exists");
        appRole = AppRole.builder().role(role).build();
        return roleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String role) {
        AppUser user = userRepository.findByUsername(username);
        AppRole roleToAdd = roleRepository.findById(role).get();
        user.getRoles().add(roleToAdd);
    }

    @Override
    public void removeRoleFromUser(String username, String role) {
        AppUser user = userRepository.findByUsername(username);
        AppRole roleToAdd = roleRepository.findById(role).get();
        user.getRoles().remove(roleToAdd);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
