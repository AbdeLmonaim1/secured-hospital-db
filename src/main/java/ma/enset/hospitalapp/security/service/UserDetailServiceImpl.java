package ma.enset.hospitalapp.security.service;

import ma.enset.hospitalapp.security.entities.AppUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private AccountService accountService;

    public UserDetailServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = accountService.loadUserByUsername(username);
        if (appUser == null) throw new UsernameNotFoundException(String.format("User %s not found", username));
        //String[] roles = appUser.getRoles().stream().map(u->u.getRole()).toArray(String[]::new);
        List<SimpleGrantedAuthority> collect = appUser.getRoles().stream().map(r-> new SimpleGrantedAuthority(r.getRole())).collect(Collectors.toList());
        //UserDetails userDetails = User.withUsername(username).password(appUser.getPassword()).roles(roles).build();
        UserDetails userDetails = User.withUsername(appUser.getUsername()).password(appUser.getPassword()).authorities(collect).build();
        return userDetails;
    }
}
