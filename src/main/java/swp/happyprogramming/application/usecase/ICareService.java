package swp.happyprogramming.application.usecase;

import swp.happyprogramming.domain.model.Care;

public interface ICareService {
    Care save(Care care);
    int deleteCare(long userId, long postId);
    int checkCared(long userId, long postId);
}
