package swp.happyprogramming.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.*;



import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

import javax.transaction.Transactional;


@Repository
public interface ICareRepository extends JpaRepository<Care,Long> {
    @Modifying
    @Transactional
    @Query(value = "delete from user_like_posts where user_id = ?1 and post_id = ?2", nativeQuery = true)
    void deleteByUserIdAndPostId(long userId, long post_id);

    @Query(value = "select * from user_like_posts where post_id = ?1 and user_id = ?2", nativeQuery = true)
    List<Care> findUserLikePost(long postId, long user_id);
}
