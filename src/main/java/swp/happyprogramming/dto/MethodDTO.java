package swp.happyprogramming.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import swp.happyprogramming.model.Method;
import swp.happyprogramming.model.User;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Data
@AllArgsConstructor
public class MethodDTO {
    private long id;
    private String name;
}
