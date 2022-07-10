package swp.happyprogramming.utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilityTest {
    @Test
    void testGetFirstLink_inSentence() {
        String message = "This message contains http://localhost.com not dot";
        String expected = "http://localhost.com";
        String actual = Utility.getFirstLink(message);
        assertEquals(expected, actual);
    }

    @Test
    void testGetFirstLink_alone() {
        String message = "http://localhost.com";
        String expected = "http://localhost.com";
        String actual = Utility.getFirstLink(message);
        assertEquals(expected, actual);
    }

    @Test
    void testGetFirstLink_mutipleLevel() {
        String message = "http://localhost.com.vn";
        String expected = "http://localhost.com.vn";
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
    void testGetFirstLink_withDot() {
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
        String message = "https protocol https://localhost.com https://localhost2.com";
        String expected = "https://localhost.com";
        String actual = Utility.getFirstLink(message);
        assertEquals(expected, actual);
    }



    @Test
    void testIsNumber1() {
        String message = "Thisisastring";
        boolean expected = false;
        boolean actual = Utility.isNumber(message);
        assertEquals(expected, actual);
    }

    @Test
    void testIsNumber2() {
        String message = "1";
        boolean expected = true;
        boolean actual = Utility.isNumber(message);
        assertEquals(expected, actual);
    }

    @Test
    void testIsNumber3() {
        String message = "-1";
        boolean expected = true;
        boolean actual = Utility.isNumber(message);
        assertEquals(expected, actual);
    }

}