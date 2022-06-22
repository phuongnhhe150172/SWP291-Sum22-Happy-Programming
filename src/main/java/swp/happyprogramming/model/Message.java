package swp.happyprogramming.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter

public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "conn_id")
    private Connect conn;

    @JoinColumn(name = "chat_content")
    private String content;

    @Column(name = "timestamp")
    private Instant timestamp;
}
