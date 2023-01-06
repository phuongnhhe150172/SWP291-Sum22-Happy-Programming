package swp.happyprogramming.adapter.port.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.domain.model.Notification;


@Repository
public interface INotification extends JpaRepository<Notification,Long> {

}
