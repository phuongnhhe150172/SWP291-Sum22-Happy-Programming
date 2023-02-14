package swp.happyprogramming.adapter.port.in.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import swp.happyprogramming.application.port.usecase.IUserService;
import swp.happyprogramming.application.services.UserService;
import swp.happyprogramming.domain.model.User;

@Controller
public class ChangePasswordController {

  @Autowired
  private UserService userService;
  @Autowired
  private IUserService iUserService;

  @GetMapping("/changePassword")
  public String changePassword(Model model) {
    System.out.println("user" + getPrincipal());
    //model.addAttribute("user", userService.findByEmail(this.getPrincipal()));
    return "admin/change-password";
  }

  @PostMapping("/saveChangePassword")
  public String saveChangePassword(HttpServletRequest request, Model model) {
    PasswordEncoder encoder = new BCryptPasswordEncoder();

    String oldPassword = request.getParameter("oldPass");
    String newPassword = request.getParameter("newPass");
    String confirmPassword = request.getParameter("confirmPass");
    System.out.println(getPrincipal());
    System.out.println("Dung:  Oldpass: " + oldPassword + "\n" +
      "new pass: " + newPassword + "\n" +
      "Confirm pass: " + confirmPassword);

    User user = userService.findByEmail(getPrincipal());
    System.out.println("passsss" + user.getPassword());
    System.out.println("passsss     " + encoder.encode(oldPassword));
    System.out.println("new" + newPassword + "and" + confirmPassword);

//        if(encoder.matches(oldPassword,user.getPasswordEncoder())){
//            System.out.println("Matched");
//        }
    if (encoder.matches(oldPassword, user.getPassword())) {
      System.out.println("matched ");

      if (newPassword.equals(confirmPassword)) {
        System.out.println("matched part 2");
        user.setPassword(newPassword);
        userService.updatePassword(user, newPassword);
        return "redirect:/auth/logout";
      }
    }
    return "redirect:/changePassword";
  }


  private String getPrincipal() {
    String userName = null;
    Object principal = SecurityContextHolder.getContext().getAuthentication()
      .getPrincipal();

    if (principal instanceof UserDetails) {
      userName = ((UserDetails) principal).getUsername();

    } else {
      userName = principal.toString();
    }
    return userName;
  }
}
