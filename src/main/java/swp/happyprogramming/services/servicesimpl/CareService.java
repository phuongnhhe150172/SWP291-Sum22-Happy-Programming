package swp.happyprogramming.services.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.RequestDTO;
import swp.happyprogramming.model.Care;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.Request;
import swp.happyprogramming.repository.ICareRepository;
import swp.happyprogramming.model.Skill;
import swp.happyprogramming.services.ICareService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CareService implements ICareService {
    @Autowired
    private ICareRepository careRepository;

    @Override
    public Care save(Care care) {
        careRepository.save(care);
        return care;
    }
    @Override
    public int deleteCare(long userId, long postId) {
        careRepository.deleteByUserIdAndPostId(userId, postId);
        return 0;
    }
    @Override
    public int checkCared(long userId, long postId) {
        if(careRepository.findUserLikePost(postId, userId).size() > 0 ) {
            return 1;
        } else {
            return 0;
        }
        
    }
}
