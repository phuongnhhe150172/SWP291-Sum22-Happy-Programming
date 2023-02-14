package swp.happyprogramming.adapter.port.out;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaRoleRepository extends JpaRepository<Role, Long> {

  @Query(value = "select * from roles where id in (select role_id from user_roles where user_id=?1)", nativeQuery = true)
  Collection<Role> getRolesByUserId(Long id);

  Role findByName(String name);
}
