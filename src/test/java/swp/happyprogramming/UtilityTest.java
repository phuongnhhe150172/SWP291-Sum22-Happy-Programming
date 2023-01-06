package swp.happyprogramming;

import org.junit.jupiter.api.Test;
import swp.happyprogramming.utility.Utility;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

// @SpringBootTest
class UtilityTest {
    @Test
    void testGetFirstLink_inSentence() {
        String message = "This message contains https://www.localhost.com not dot";
        String expected = "https://www.localhost.com";
        String actual = Utility.getFirstLink(message);
        assertEquals(expected, actual);
    }

    @Test
    void testGetFirstLink_alone() {
        String message = "https://www.localhost.com";
        String expected = "https://www.localhost.com";
        String actual = Utility.getFirstLink(message);
        assertEquals(expected, actual);
    }

    @Test
    void testGetFirstLink_mutipleLevel() {
        String message = "https://www.localhost.com.vn";
        String expected = "https://www.localhost.com.vn";
        String actual = Utility.getFirstLink(message);
        assertEquals(expected, actual);
    }

    @Test
    void testGetFirstLink_parameters() {
        String message = "https://www.youtube.com/watch?v=O6aNB8x2gYE&t=242s";
        String expected = "https://www.youtube.com/watch?v=O6aNB8x2gYE&t=242s";
        String actual = Utility.getFirstLink(message);
        assertEquals(expected, actual);
    }

    @Test
    void testGetFirstLink_noLink() {
        String message = "This message contains no link. It is a test";
        String expected = "";
        String actual = Utility.getFirstLink(message);
        assertEquals(expected, actual);
    }

    @Test
    void testGetFirstLink_mutipleLinks() {
        String message = "2 links http://localhost.com not dot. Also http://localhost2.com";
        String expected = "http://localhost.com";
        String actual = Utility.getFirstLink(message);
        assertEquals(expected, actual);
    }

    @Test
    void testGetFirstLink_textWithDot() {
        String message = "2 links with dot http://localhost.com. Also http://localhost2.com";
        String expected = "http://localhost.com";
        String actual = Utility.getFirstLink(message);
        assertEquals(expected, actual);
    }

    @Test
    void testGetFirstLink_mutipleSpaces() {
        String message = "mutiple spaces   http://localhost.com    not dot.   http://localhost2.com";
        String expected = "http://localhost.com";
        String actual = Utility.getFirstLink(message);
        assertEquals(expected, actual);
    }

    @Test
    void testGetFirstLink_otherSpacings() {
        String message = "mutiple spaces\thttp://localhost.com\nhttp://localhost2.com";
        String expected = "http://localhost.com";
        String actual = Utility.getFirstLink(message);
        assertEquals(expected, actual);
    }

    @Test
    void testGetFirstLink_otherProtocol() {
        String message = "http protocol http://localhost.com http://localhost2.com";
        String expected = "http://localhost.com";
        String actual = Utility.getFirstLink(message);
        assertEquals(expected, actual);
    }

    @Test
    void testIsInteger_string() {
        String message = "Thisisastring";
        boolean expected = false;
        boolean actual = Utility.isInteger(message);
        assertEquals(expected, actual);
    }

    @Test
    void testIsInteger_positive() {
        String message = "1";
        boolean expected = true;
        boolean actual = Utility.isInteger(message);
        assertEquals(expected, actual);
    }

    @Test
    void testIsInteger_negative() {
        String message = "-1";
        boolean expected = true;
        boolean actual = Utility.isInteger(message);
        assertEquals(expected, actual);
    }

    @Test
    void testIsInteger_zero() {
        String message = "0";
        boolean expected = true;
        boolean actual = Utility.isInteger(message);
        assertEquals(expected, actual);
    }

    @Test
    void testIsInteger_float() {
        String message = "1.23";
        boolean expected = false;
        boolean actual = Utility.isInteger(message);
        assertEquals(expected, actual);
    }

    @Test
    void testIsInteger_commaFloat() {
        String message = "100,000";
        boolean expected = false;
        boolean actual = Utility.isInteger(message);
        assertEquals(expected, actual);
    }

    @Test
    void testAddOG_noOG() {
        String testContent = "This is a test";
        Map map = new HashMap();
        map.put("content", testContent);
        try {
            Utility.addOG(map);
        } catch (IOException e) {
            fail();
        }
        assertNull(map.get("link"));
        assertNull(map.get("title"));
        assertNull(map.get("image"));
    }

    @Test
    void testAddOG_onlyLink() {
        String testContent = "https://www.youtube.com/";
        Map map = new HashMap();
        map.put("content", testContent);
        try {
            Utility.addOG(map);
        } catch (IOException e) {
            fail();
        }
        assertEquals(map.get("link"), testContent);
        assertEquals("YouTube", map.get("title"));
        assertEquals("https://www.youtube.com/img/desktop/yt_1200.png", map.get("image"));
    }

    @Test
    void testAddOG_withSurroundingText() {
        String testContent = "This is https://www.youtube.com/ for Youtube";
        Map map = new HashMap();
        map.put("content", testContent);
        try {
            Utility.addOG(map);
        } catch (IOException e) {
            fail();
        }
        assertEquals("https://www.youtube.com/", map.get("link"));
        assertEquals("YouTube", map.get("title"));
        assertEquals("https://www.youtube.com/img/desktop/yt_1200.png", map.get("image"));
    }

    @Test
    void testAddOG_noImage() {
        String testContent = "https://www.google.com/";
        Map map = new HashMap();
        map.put("content", testContent);
        try {
            Utility.addOG(map);
        } catch (IOException e) {
            fail();
        }
        assertEquals("https://www.google.com/", map.get("link"));
        assertEquals("Google", map.get("title"));
        assertEquals("/upload/static/imgs/noimage.jpg", map.get("image"));
    }

    @Test
    void testAddOG_noImageWithSurroundingText() {
        String testContent = "This is https://www.google.com/ for Google";
        Map map = new HashMap();
        map.put("content", testContent);
        try {
            Utility.addOG(map);
        } catch (IOException e) {
            fail();
        }
        assertEquals("https://www.google.com/", map.get("link"));
        assertEquals("Google", map.get("title"));
        assertEquals("/upload/static/imgs/noimage.jpg", map.get("image"));
    }
}