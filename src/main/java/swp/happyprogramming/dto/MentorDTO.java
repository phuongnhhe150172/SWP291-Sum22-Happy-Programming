package swp.happyprogramming.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import swp.happyprogramming.model.Experience;
import org.springframework.format.annotation.DateTimeFormat;
import swp.happyprogramming.model.Skill;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
public class MentorDTO {
    private long id;
    private long profileId;
    private String fullName;
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Skill> skills;
    private ArrayList<ExperienceDTO> experiences;
//    private ArrayList<Skill> skills;
    private int gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;
    private String phoneNumber;
    private String ward;
    private String district;
    private String province;
    private String street;
    private String bio;
    private String school;
//    private String method;
    private float price;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date created;
    private Date modified;

    public MentorDTO() {
        this.created = Date.from(Instant.now());
        this.modified = Date.from(Instant.now());
        this.skills = new ArrayList<>();
        this.experiences = new ArrayList<>();
    }
}
