package swp.happyprogramming.adapter.port.out;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.application.port.out.MethodPortOut;
import swp.happyprogramming.domain.model.Method;

@Repository("methodRepository")
public interface IMethodRepository extends JpaRepository<Method, Long> ,
  MethodPortOut {

  @Query(value = "select * from method", nativeQuery = true)
  List<Method> getListMethod();

  Method findById(long id);
}
