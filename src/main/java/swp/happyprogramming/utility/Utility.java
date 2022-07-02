package swp.happyprogramming.utility;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.modelmapper.ModelMapper;
import swp.happyprogramming.dto.*;
import swp.happyprogramming.model.Address;
import swp.happyprogramming.model.Feedback;
import swp.happyprogramming.model.User;
import swp.happyprogramming.model.Ward;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

    public static String[] getOG(String website) throws IOException {
        if (website.isEmpty()) return new String[0];
        Document doc = Jsoup.connect(website)
                // .userAgent(USER_AGENT)
                .header("Accept-Encoding", "gzip,deflate,sdch")
                .timeout(0)
                .maxBodySize(0)
                .get();
        String[] og = new String[2];
        if (doc.selectFirst("title") != null) {
            og[0] = doc.selectFirst("title").text();
        } else {
            og[0] = "Untitled";
        }
        Element imageTag = doc.selectFirst("meta[property='og:image']");
        if (imageTag != null && imageTag.attr("content") != null) {
            og[1] = imageTag.attr("content");
        } else {
            og[1] = "/upload/static/imgs/noimage.jpg";
        }
        return og;
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
