package swp.happyprogramming.domain.model;

import lombok.Data;

import jakarta.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @Column(name = "id", nullable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    public Role(int id) {
        this.id = id;
        if (id == 1) {
            this.name = "ROLE_MENTOR";
        } else {
            this.name = "ROLE_MENTEE";
        }
    }

    public Role() {
    }

    public Role(int id, String name){
        this.id = id;
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"))
    private Collection<User> users;



    @ManyToMany(mappedBy = "notificationToRoles")
    private Collection<Notification> notifications;
}
