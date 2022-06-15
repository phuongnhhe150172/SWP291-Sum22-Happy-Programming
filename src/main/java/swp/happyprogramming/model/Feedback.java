package swp.happyprogramming.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "feedback")
@Data
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "received_id")
    private long receiverid;

    @Column(name = "sender_id")
    private long senderid;

    @Column(name = "rate")
    private int rate;

    @Column(name = "comment")
    private long comment;
}
