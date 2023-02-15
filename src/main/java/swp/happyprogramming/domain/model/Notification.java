package swp.happyprogramming.domain.model;


import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "notification")
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id = 0L;

    @Column(name = "content")
    private String content;

    @Column(name = "created")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date created;

    @Column(name = "modified")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modified;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "role_has_notification",
            joinColumns = @JoinColumn(
                    name = "noti_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> notificationToRoles;


    public Notification(){
        created = Date.from(Instant.now());
        modified = Date.from(Instant.now());
    }

}
