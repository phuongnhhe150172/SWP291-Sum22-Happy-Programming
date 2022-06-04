package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.Post;

@Repository
public interface IPostRepository extends JpaRepository<Post,Long> {
}
