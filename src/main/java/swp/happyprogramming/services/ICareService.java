package swp.happyprogramming.services;

import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.Care;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ICareService {
    Care save(Care care);
    int deleteCare(long userId, long postId);
    int checkCared(long userId, long postId);
}
