package swp.happyprogramming.application.port.out;

import java.util.Collection;
import swp.happyprogramming.domain.model.Role;

public interface RolePortOut {

  Collection<Role> getRolesByUserId(Long id);

  Role findByName(String name);
}