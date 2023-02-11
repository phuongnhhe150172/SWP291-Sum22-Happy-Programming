package swp.happyprogramming.utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import swp.happyprogramming.adapter.dto.AddressDTO;
import swp.happyprogramming.adapter.dto.DistrictDTO;
import swp.happyprogramming.adapter.dto.MentorDTO;
import swp.happyprogramming.adapter.dto.NotificationDTO;
import swp.happyprogramming.adapter.dto.ProvinceDTO;
import swp.happyprogramming.adapter.dto.UserAvatarDTO;
import swp.happyprogramming.adapter.dto.UserDTO;
import swp.happyprogramming.adapter.dto.WardDTO;
import swp.happyprogramming.domain.model.Address;
import swp.happyprogramming.domain.model.Experience;
import swp.happyprogramming.domain.model.Feedback;
import swp.happyprogramming.domain.model.Mentor;
import swp.happyprogramming.domain.model.Notification;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Skill;
import swp.happyprogramming.domain.model.User;

public class Utility {

  private static final ModelMapper mapper = new ModelMapper();
  private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0";
  private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

  private Utility() {
    //    Overwrite default constructor
  }

  public static String getSiteURL(HttpServletRequest request) {
    String siteURL = request.getRequestURL().toString();
    return siteURL.replace(request.getServletPath(), "");
  }

  public static UserDTO mapUser(User user) {
    if (user == null) {
      return null;
    }
    if (user.getId() == null) {
      return null;
    }
    if (user.getAddress() == null) {
      return null;
    }
    UserDTO userDTO = mapper.map(user, UserDTO.class);
    userDTO.setAddress(mapAddress(user.getAddress()));
    return userDTO;
  }

  public static MentorDTO mapMentor(Mentor mentor) {
    if (mentor == null) {
      return null;
    }
    MentorDTO mentorDTO = mapper.map(mentor, MentorDTO.class);
    mapper.map(mentor.getUser(), mentorDTO);
    mentorDTO.setProfileId(mentor.getId());
    mentorDTO.setExperiences((List<Experience>) mentor.getExperiences());
    mentorDTO.setSkills((List<Skill>) mentor.getSkills());
    mentorDTO.setAddress(Utility.mapAddress(mentor.getUser().getAddress()));
    return mentorDTO;
  }

  public static List<MentorDTO> mapMentors(List<Mentor> mentors) {
    return mentors.stream().map(Utility::mapMentor).collect(Collectors.toList());
  }

  public static AddressDTO mapAddress(Address address) {
    AddressDTO addressDTO = mapper.map(address, AddressDTO.class);
    addressDTO.setWard(mapper.map(address.getWard(), WardDTO.class));
    addressDTO.setDistrict(mapper.map(address.getWard().getDistrict(), DistrictDTO.class));
    addressDTO.setProvince(
      mapper.map(address.getWard().getDistrict().getProvince(), ProvinceDTO.class));
    return addressDTO;
  }

  public static double getAverageRate(List<Feedback> feedback) {
    if (feedback.isEmpty()) {
      return 0;
    }
    return (double) feedback.stream().mapToInt(Feedback::getRate).sum() / feedback.size();
  }

  public static void addOG(Map value) throws IOException {
    String firstURL = getFirstLink((String) value.get("content"));
    if (firstURL.isEmpty()) {
      return;
    }
    value.put("link", firstURL);
    Document doc = Jsoup.connect(firstURL)
      // .userAgent(USER_AGENT)
      .header("Accept-Encoding", "gzip,deflate,sdch").timeout(0).maxBodySize(0)
      .ignoreContentType(true).get();
    if (doc.selectFirst("title") != null) {
      value.put("title", doc.selectFirst("title").text());
    } else {
      value.put("title", "Untitled");
    }
    Element imageTag = doc.selectFirst("meta[property='og:image']");
    if (imageTag != null && imageTag.attr("content") != null) {
      value.put("image", imageTag.attr("content"));
    } else {
      value.put("image", "/upload/static/imgs/noimage.jpg");
    }
  }

  public static String getFirstLink(String content) {
    String regex = "^https?://(?:www\\.)?[-a-zA-Z0-9@:%._+~#=]+\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$";

    String[] words = content.split("\\s+");
    for (String word : words) {
      if (!word.matches(regex)) {
        continue;
      }
      if (word.endsWith(".")) {
        return word.substring(0, word.length() - 1);
      }
      return word;
    }
    return "";
  }

  public static boolean isInteger(String str) {
    try {
      Integer.parseInt(str);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static NotificationDTO mapNotification(Notification notification) {
    NotificationDTO notificationDTO = mapper.map(notification, NotificationDTO.class);
    Date today = new Date();

    LocalDateTime todayDateTime = today.toInstant().atZone(ZoneId.systemDefault())
      .toLocalDateTime();
    LocalDateTime modifiedDateTime = notification.getModified().toInstant()
      .atZone(ZoneId.systemDefault()).toLocalDateTime();

    long secondsElapsed = ChronoUnit.SECONDS.between(modifiedDateTime, todayDateTime);
    long minutesElapsed = ChronoUnit.MINUTES.between(modifiedDateTime, todayDateTime);
    long hoursElapsed = ChronoUnit.HOURS.between(modifiedDateTime, todayDateTime);
    long daysElapsed = ChronoUnit.DAYS.between(modifiedDateTime, todayDateTime);
    long weeksElapsed = ChronoUnit.WEEKS.between(modifiedDateTime, todayDateTime);
    long monthsElapsed = ChronoUnit.MONTHS.between(modifiedDateTime, todayDateTime);
    long yearsElapsed = ChronoUnit.YEARS.between(modifiedDateTime, todayDateTime);
    if (yearsElapsed > 0) {
      if (yearsElapsed == 1) {
        notificationDTO.setTime(yearsElapsed + " year ago");
      } else {
        notificationDTO.setTime(yearsElapsed + " years ago");
      }
    } else if (monthsElapsed > 0) {
      if (monthsElapsed == 1) {
        notificationDTO.setTime(monthsElapsed + " month ago");
      } else {
        notificationDTO.setTime(monthsElapsed + " months ago");
      }
    } else if (weeksElapsed > 0) {
      if (weeksElapsed == 1) {
        notificationDTO.setTime(weeksElapsed + " week ago");
      } else {
        notificationDTO.setTime(weeksElapsed + " weeks ago");
      }
    } else if (daysElapsed > 0) {
      if (daysElapsed == 1) {
        notificationDTO.setTime(daysElapsed + " days ago");
      } else {
        notificationDTO.setTime(daysElapsed + " days ago");
      }
    } else if (hoursElapsed > 0) {
      if (hoursElapsed == 1) {
        notificationDTO.setTime(hoursElapsed + " hours ago");
      } else {
        notificationDTO.setTime(hoursElapsed + " hours ago");
      }
    } else if (minutesElapsed > 0) {
      if (minutesElapsed == 1) {
        notificationDTO.setTime(minutesElapsed + " minutes ago");
      } else {
        notificationDTO.setTime(minutesElapsed + " minutes ago");
      }
    } else {
      if (secondsElapsed == 1) {
        notificationDTO.setTime(secondsElapsed + " second ago");
      } else {
        notificationDTO.setTime(secondsElapsed + " seconds ago");
      }
    }
    return notificationDTO;
  }

  public static UserAvatarDTO mapUserToAvatarDTO(User user) {
    return new UserAvatarDTO(user.getId(), user.getFirstName() + " " + user.getLastName(),
      user.getImage());
  }

  public static List<UserAvatarDTO> mapUsersToAvatarDTO(List<User> users) {
    return users.stream().map(Utility::mapUserToAvatarDTO).collect(Collectors.toList());
  }

  public static UserAvatarDTO mapMentorToAvatarDTO(Mentor mentor) {
    return new UserAvatarDTO(mentor.getUser().getId(),
      mentor.getUser().getFirstName() + " " + mentor.getUser().getLastName(),
      mentor.getUser().getImage());
  }

  public static <V> Pagination<V> getPagination(int totalPages, List<V> connections) {
    List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
      .collect(Collectors.toList());
    return new Pagination<>(connections, pageNumbers);
  }

  public static <V> Pagination<V> getPagination(Page<V> page) {
    List<V> content = page.getContent();
    int totalPages = page.getTotalPages();
    List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
      .collect(Collectors.toList());
    return new Pagination<>(content, pageNumbers);
  }

  public static <K, V> Pagination<V> getPagination(Page<K> page, Transformer<K, V> transformer) {
    List<K> content = page.getContent();
    int totalPages = page.getTotalPages();
    List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
      .collect(Collectors.toList());
    List<V> transformed = content.stream().map(transformer::transform).collect(Collectors.toList());
    return new Pagination<>(transformed, pageNumbers);
  }

  public static void saveImage(MultipartFile image, String imageName) {
    Path imagesPath = Paths.get("src/main/upload/static/imgs");
    Path imagePath = Paths.get(imageName);
    Path file = CURRENT_FOLDER.resolve(imagesPath).resolve(imagePath);
    try {
      Files.write(file, image.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
