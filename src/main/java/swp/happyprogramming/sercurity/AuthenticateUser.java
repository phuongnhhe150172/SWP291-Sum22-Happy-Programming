package swp.happyprogramming.sercurity;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import swp.happyprogramming.model.Role;
import swp.happyprogramming.model.User;

import java.util.Collection;

public class AuthenticateUser extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = 1L;

    private User user;

    public AuthenticateUser(User user) {
        super(user.getEmail(), user.getPassword(), getAuthorities(user));
        this.user = user;
    }

    public User getUser() {
        return user;
    }


    private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
        Set<String> roleAndPermissions = new HashSet<>();
        List<Role> roles = (List<Role>) user.getRoles();
        roles.forEach(role -> roleAndPermissions.add(role.getName()));
        String[] roleNames = new String[roleAndPermissions.size()];
        return AuthorityUtils.createAuthorityList(roleAndPermissions.toArray(roleNames));
    }
}
