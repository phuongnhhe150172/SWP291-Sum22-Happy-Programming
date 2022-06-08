package swp.happyprogramming.sercurity;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Address;
import swp.happyprogramming.model.User;
import swp.happyprogramming.repository.IAddressRepository;
import swp.happyprogramming.services.IUserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

@Configuration
public class SimpleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private IUserService userService;

    @Autowired
    private IAddressRepository addressRepository;

    ModelMapper mapper = new ModelMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        authorities.forEach(authority -> {
            if (authority.getAuthority().equals("ROLE_MENTOR")) {
                try {
                    String sessionRole = "MENTOR_AND_MENTEE";
                    session.setAttribute("role", sessionRole);
                    session.setAttribute("id",userDTO.getId());
                    session.setAttribute("userInformation", userDTO);
                    redirectStrategy.sendRedirect(request, response, "/home");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (authority.getAuthority().equals("ROLE_MENTEE")) {
                try {
                    String sessionRole = "MENTEE";
                    session.setAttribute("role", sessionRole);
                    session.setAttribute("id",userDTO.getId());
                    session.setAttribute("userInformation", userDTO);
                    redirectStrategy.sendRedirect(request, response, "/home");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (authority.getAuthority().contains("ROLE_ADMIN")) {
                try {
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
