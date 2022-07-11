package swp.happyprogramming.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.*;
import java.util.Collection;

@Entity
@IdClass(embedCare.class)
@Table(name = "user_like_posts")
@Getter
@Setter
public class Care {

    @Id
    @Column(name = "user_id")
    private long userId;

    @Id
    @Column(name = "post_id")
    private long postId;

    @Column(name = "created")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date created;

    @Column(name = "modified")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modified;

    public Care(){
        created = Date.from(Instant.now());
        modified = Date.from(Instant.now());
    }

}


