package swp.happyprogramming.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Pagination<T> {
    List<T> paginatedList;
    List<Integer> pageNumbers;
}
