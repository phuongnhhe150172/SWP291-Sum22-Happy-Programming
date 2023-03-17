package swp.happyprogramming.adapter.sercurity;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import swp.happyprogramming.application.dto.UserDTO;
import swp.happyprogramming.application.port.usecase.IUserService;
import swp.happyprogramming.domain.model.User;
import swp.happyprogramming.utility.Utility;

@Configuration
public class SimpleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private IUserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        UserDTO userDTO = Utility.mapUser(user);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (user.getStatus() == 0){
               redirectStrategy.sendRedirect(request, response, "/login?msg");
        }else{
            authorities.forEach(authority -> {
                session.setAttribute("id", userDTO.getId());
                session.setAttribute("userInformation", userDTO);
                if (authority.getAuthority().equals("ROLE_MENTOR")) {
                    try {
                        String sessionRole = "MENTOR_AND_MENTEE";
                        session.setAttribute("role", sessionRole);
                        redirectStrategy.sendRedirect(request, response, "/home");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (authority.getAuthority().equals("ROLE_MENTEE")) {
                    try {
                        String sessionRole = "MENTEE";
                        session.setAttribute("role", sessionRole);
                        redirectStrategy.sendRedirect(request, response, "/home");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (authority.getAuthority().contains("ROLE_ADMIN")) {
                    try {
                        String sessionRole = "ADMIN";
                        session.setAttribute("role", sessionRole);
                        redirectStrategy.sendRedirect(request, response, "/admin/dashboard");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    throw new IllegalStateException();
                }
            });
        }
    }
}
