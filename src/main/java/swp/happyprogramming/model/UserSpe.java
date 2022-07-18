package swp.happyprogramming.model;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class UserSpe {

    public static Specification<User> getUserSpe(String firstName, String lastName, String phone, String email, ArrayList<Role> roles) {
        return (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            Join<User, Role> userRoles = root.join("roles");
            list.add(cb.equal(userRoles.get("id"), 2));
            if (firstName != null) {
                list.add(cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%"));
            }
            if (lastName != null) {
                list.add(cb.like(cb.lower(root.get("firstName")), "%" + lastName.toLowerCase() + "%"));
            }
            if (phone != null) {
                list.add(cb.like(cb.lower(root.get("firstName")), "%" + phone.toLowerCase() + "%"));
            }
            if (email != null) {
                list.add(cb.like(cb.lower(root.get("firstName")), "%" + email.toLowerCase() + "%"));
            }
            //
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
    }
}
