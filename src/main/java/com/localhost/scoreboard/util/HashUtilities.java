package com.localhost.scoreboard.util;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

public class HashUtilities {

    public static String getHash(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest);
        } catch (Exception e) {
            return "";
        }
    }
}
