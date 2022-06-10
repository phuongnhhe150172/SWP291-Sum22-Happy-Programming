package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.model.Method;
import swp.happyprogramming.repository.IMethodRepository;
import swp.happyprogramming.services.IMethodService;

import java.util.List;

@Service
public class MethodService implements IMethodService {
    @Autowired
    private IMethodRepository methodRepository;

    @Override
    public List<Method> getAllMethod() {
        return methodRepository.getListMethod();
    }
}
