package ma.enset.hospitalapp.security;

import ma.enset.hospitalapp.security.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private PasswordEncoder passwordEncoder;
    private UserDetailServiceImpl userDetailServiceImpl;

    public SecurityConfig(PasswordEncoder passwordEncoder, UserDetailServiceImpl userDetailServiceImpl) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailServiceImpl = userDetailServiceImpl;
    }

    //Ici si on utilise UserDetailsService a la place de InMemoryUserDetailsManager
    //Quand on a un user va saisie son username et password, Spring Security appelle a cette methode 'loadUserByUsername',
    //je va utiliser une interface userRepository contient une metode 'findByUsername' qui va chercher les users dans DB,
    //Ici on va utiliser UserDetailsService
    //@Bean
//    public UserDetailsService userDetailsService() {
//        return new UserDetailsService() {
//            @Override
//            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//                return null;
//            }
//        };
//    }
    //@Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        //il faut specifie le dataSource Oui se trouve les utilisateurs et les roles
        return new JdbcUserDetailsManager(dataSource);
    }
    //@Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        //Les users et leur roles sont stockes en InMemory
        //Pour indiquer quel sont les users qui ont le droite d'acceder a l'app. {noop} c'est pour ne pas utiliser un mdp encoder
        return new InMemoryUserDetailsManager(
                //User.withUsername("mouad").password("{noop}22222").roles("USER").build(),
                User.withUsername("mouad").password(passwordEncoder.encode("22222")).roles("USER").build(),
                User.withUsername("amine").password(passwordEncoder.encode("amine")).roles("USER").build(),
                User.withUsername("Monaim").password(passwordEncoder.encode("monaim11")).roles("USER","ADMIN").build()
        );
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //Pour ajouter une formulaire d'authentification/.loginPage()C'est pour personnaliser la formulaire d'athentification(Creer mon propre formulaire login)
        //.permitAll() C'est pour dire que ce EndPoint (/login) est autoriser pour tout le monde
        httpSecurity.formLogin().loginPage("/login").defaultSuccessUrl("/").permitAll();
        //Pour gerer les droites d'acces (autorisation)
        //Toutes les requettes HTTP dans le path contient /user/qlq chose necessaite d'avoir un role USER
        //httpSecurity.authorizeHttpRequests().requestMatchers("/user/**").hasRole("USER");
        //Toutes les requettes HTTP dans le path contient /admin/qlq chose necessaite d'avoir un role ADMIN
        //httpSecurity.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN");
        httpSecurity.authorizeHttpRequests().requestMatchers("/webjars/**").permitAll();
        httpSecurity.rememberMe();
        //Cela pour handle des exception si vous avez pas le droite on va tu dirige vers cette page
        httpSecurity.exceptionHandling().accessDeniedPage("/notAuthorized");
        //c-a-d que vous avez entrain de dire a Spring Securty je vous drais que toutes les requettes necessaite une Authentiication
        httpSecurity.authorizeHttpRequests().anyRequest().authenticated();
        //C'est pour utiliser la strategie de UserDetailsService
        httpSecurity.userDetailsService(userDetailServiceImpl);
        return httpSecurity.build();
    }
}
