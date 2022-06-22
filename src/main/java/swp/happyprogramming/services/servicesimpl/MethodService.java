package swp.happyprogramming.services.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import swp.happyprogramming.dto.MethodDTO;
import swp.happyprogramming.model.Method;
import swp.happyprogramming.repository.IMethodRepository;
import swp.happyprogramming.services.IMethodService;

import org.modelmapper.ModelMapper;


import swp.happyprogramming.utility.Utility;
import java.util.List;

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
