package swp.happyprogramming.application.usecase;

import swp.happyprogramming.adapter.dto.MethodDTO;
import swp.happyprogramming.domain.model.Method;

import java.util.List;

public interface IMethodService {
    public List<Method> getAllMethod();
    public MethodDTO findMethod(long id);
}
