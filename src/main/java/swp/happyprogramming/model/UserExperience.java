package swp.happyprogramming.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "user_roles")
@Getter
@Setter
@ToString
public class UserExperience {
    @Column(name = "user_id")
    private long userId;

    @Column(name = "role_id")
    private long roleId;
}
