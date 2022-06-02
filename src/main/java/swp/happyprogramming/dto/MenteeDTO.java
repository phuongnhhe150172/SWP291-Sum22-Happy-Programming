package swp.happyprogramming.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
public class MenteeDTO {

    private long id;
    private long profileId;
    private String fullName;
    private String firstName;
    private String lastName;
    private String email;

    private int gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;
    private String phoneNumber;
    private long addressId;
    private String ward;
    private String district;
    private String province;
    private String street;
    private String bio;
    private String school;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date created;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modified;

    public void MentorDTO() {
        this.modified = Date.from(Instant.now());
        this.modified = Date.from(Instant.now());
    }
}
