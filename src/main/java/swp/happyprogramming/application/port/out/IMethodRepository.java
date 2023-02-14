package swp.happyprogramming.application.port.out;

import java.util.List;
import swp.happyprogramming.domain.model.Method;


public interface IMethodRepository {

  List<Method> getListMethod();

  Method findById(long id);
}
