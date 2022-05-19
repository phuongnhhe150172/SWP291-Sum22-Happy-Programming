package swp391_sum22.happyprogramming.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class MentorDTO {
    private String fullname;
    private int gender;
    private Date dob;
    private String phone_number;
    private String address;
    private String bio;
    private String school;
    private String method;
    private float price;
    private Date created;
}
