package swp.happyprogramming.application.services;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.adapter.port.out.IMethodRepository;
import swp.happyprogramming.application.usecase.IMethodService;
import swp.happyprogramming.domain.dto.MethodDTO;
import swp.happyprogramming.domain.model.Method;

@Service
public class MethodService implements IMethodService {
    @Autowired
    private IMethodRepository methodRepository;

    ModelMapper mapper = new ModelMapper();

    @Override
    public List<Method> getAllMethod() {
        return methodRepository.getListMethod();
    }

    @Override
    public MethodDTO findMethod(long id) {
        Method method = methodRepository.findById(id);
        if (method == null) return null;
        MethodDTO postDTO = mapper.map(method, MethodDTO.class);
        return postDTO;
    }
}
