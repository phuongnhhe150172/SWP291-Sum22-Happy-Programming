package swp.happyprogramming.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.adapter.port.out.ICareRepository;
import swp.happyprogramming.application.usecase.ICareService;
import swp.happyprogramming.domain.model.Care;

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
