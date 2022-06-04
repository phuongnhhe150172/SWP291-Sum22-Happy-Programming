package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.MethodDTO;
import swp.happyprogramming.repository.IMethodRepository;
import swp.happyprogramming.services.IMethodService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MethodService implements IMethodService {
    @Autowired
    private IMethodRepository methodRepository;

    @Override
    public List<MethodDTO> getAllMethod() {
        ModelMapper mapper = new ModelMapper();
        return methodRepository.getListMethod().stream()
                .map(value -> mapper.map(value,MethodDTO.class)).collect(Collectors.toList());
    }
}
