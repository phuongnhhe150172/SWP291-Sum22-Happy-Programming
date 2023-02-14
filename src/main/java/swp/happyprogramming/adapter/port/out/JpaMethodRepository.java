package swp.happyprogramming.adapter.port.out;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMethodRepository extends JpaRepository<Method, Long> {

  @Query(value = "select * from method", nativeQuery = true)
  List<Method> getListMethod();

  Method findById(long id);
}
