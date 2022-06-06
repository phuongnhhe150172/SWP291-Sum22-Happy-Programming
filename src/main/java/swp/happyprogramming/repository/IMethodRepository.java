package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.Method;

import java.util.List;

@Repository
public interface IMethodRepository  extends JpaRepository<Method,Long> {
    @Query(value = "select * from method",nativeQuery = true)
    List<Method> getListMethod();

    Method findById(long id);
}
