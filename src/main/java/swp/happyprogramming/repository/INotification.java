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
public interface INotification extends JpaRepository<Notification,Long> {

}
