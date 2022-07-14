package swp.happyprogramming.model;

import org.springframework.data.jpa.domain.Specification;

public class UserSpe {

    public static Specification<User> getUserSpe(String firstName, String lastName, String phone, String email){
        Specification<User> spec = null;
        Specification<User> temp = null;

        if (firstName != null){
            temp = getByColumn("firstName", firstName);
            spec = spec!=null?Specification.where(spec).and(temp):temp;
        }
        if (lastName != null){
            temp = getByColumn("lastName", lastName);
            spec = spec!=null?Specification.where(spec).and(temp):temp;
        }
        if (phone != null){
            temp = getByColumn("phoneNumber", phone);
            spec = spec!=null?Specification.where(spec).and(temp):temp;
        }
        if (email != null) {
            temp = getByColumn("email", email);
            spec = spec != null ? Specification.where(spec).and(temp) : temp;
        }
        return spec;
    }

    private static<T> Specification<T> getByColumn(String columnName, Object value) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(columnName)), "%" + value.toString().toLowerCase() + "%");
        };
    }
}
