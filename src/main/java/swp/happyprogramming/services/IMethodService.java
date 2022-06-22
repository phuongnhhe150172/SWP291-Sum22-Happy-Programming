package swp.happyprogramming.services;

import swp.happyprogramming.dto.MethodDTO;
import swp.happyprogramming.model.Method;

import java.util.List;

public interface IMethodService {
    public List<Method> getAllMethod();
    public MethodDTO findMethod(long id);
}
