package swp.happyprogramming.controllers.mentee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import swp.happyprogramming.dto.WardDTO;
import swp.happyprogramming.model.User;
import swp.happyprogramming.model.UserProfile;
import swp.happyprogramming.model.Ward;
import swp.happyprogramming.services.servicesimpl.UserService;
import swp.happyprogramming.services.servicesimpl.WardService;

import java.util.List;

@Controller
public class MenteeController {
    @Autowired
    private UserService userService;

    @Autowired
    private WardService wardService;

    @GetMapping("/updateUser/{id}")
    public String updateUser(@Param(value = "id") long id, Model model){
       User user = userService.findById(id);
       model.addAttribute("firstName", user.getFirstName());
       model.addAttribute("lastName", user.getLastName());

       UserProfile userProfile = userService.findProfileByUserID(id).get();
       model.addAttribute("gender", userProfile.getGender());
       model.addAttribute("dob",userProfile.getDob());
       model.addAttribute("phoneNumber", userProfile.getPhoneNumber());

       return "/mentee/UpdateMenteeProfile";
    }

}
