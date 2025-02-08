package ma.enset.hospitalapp.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
//Pour appliquer notion d'authorities a la place de Roles
@RestController
public class SecurityRestController {
    @GetMapping("/profile")
    public Authentication authentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth;
    }
}
