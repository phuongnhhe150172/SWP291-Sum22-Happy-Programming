package swp.happyprogramming.utility;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.modelmapper.ModelMapper;
import swp.happyprogramming.dto.*;
import swp.happyprogramming.model.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Utility {

    private static final ModelMapper mapper = new ModelMapper();
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0";

    private Utility() {
        //    Overwrite default constructor
    }

    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    public static UserDTO mapUser(User user) {
        if (user == null) return null;
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        userDTO.setAddress(mapAddress(user.getAddress()));
        return userDTO;
    }

    public static MentorDTO mapMentor(Mentor mentor) {
        if (mentor == null) return null;
        MentorDTO mentorDTO = mapper.map(mentor, MentorDTO.class);
        mapper.map(mentor.getUser(), mentorDTO);
        mentorDTO.setProfileId(mentor.getId());
        mentorDTO.setExperiences((List<Experience>) mentor.getExperiences());
        mentorDTO.setSkills((List<Skill>) mentor.getSkills());
        mentorDTO.setAddress(Utility.mapAddress(mentor.getUser().getAddress()));
        return mentorDTO;
    }

    public static AddressDTO mapAddress(Address address) {
        AddressDTO addressDTO = mapper.map(address, AddressDTO.class);
        addressDTO.setWard(mapper.map(address.getWard(), WardDTO.class));
        addressDTO.setDistrict(mapper.map(address.getWard().getDistrict(), DistrictDTO.class));
        addressDTO.setProvince(mapper.map(address.getWard().getDistrict().getProvince(), ProvinceDTO.class));
        return addressDTO;
    }

    public static double getAverageRate(List<Feedback> feedback) {
        if (feedback.isEmpty()) return 0;
        return (double) feedback.stream().mapToInt(Feedback::getRate).sum() / feedback.size();
    }

    public static void addOG(Map value) throws IOException {
        String firstURL = getFirstLink((String) value.get("content"));
        Document doc = Jsoup.connect(firstURL)
                // .userAgent(USER_AGENT)
                .header("Accept-Encoding", "gzip,deflate,sdch")
                .timeout(0)
                .maxBodySize(0)
                .get();
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
        String regex = "^https?://(?:www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$";

        String[] words = content.split(" ");
        for (String word : words) {
            if (word.matches(regex)) {
                return word;
            }
        }
        return "";
    }
}
