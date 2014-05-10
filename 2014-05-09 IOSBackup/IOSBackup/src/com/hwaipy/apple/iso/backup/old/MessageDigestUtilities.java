package com.hwaipy.apple.iso.backup.old;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Hwaipy
 */
public class MessageDigestUtilities {

    private static final ThreadLocal<Map<String, MessageDigest>> MAP = new ThreadLocal<>();

    public static byte[] hash(String algorithm, byte[] data) throws NoSuchAlgorithmException {
        Map<String, MessageDigest> map = MAP.get();
        if (map == null) {
            map = new HashMap<>();
            MAP.set(map);
        }
        MessageDigest messageDigest = map.get(algorithm);
        if (messageDigest == null) {
            messageDigest = MessageDigest.getInstance(algorithm);
            map.put(algorithm, messageDigest);
        }
        return messageDigest.digest(data);
    }

    public static String hashForString(String algorithm, byte[] data) throws NoSuchAlgorithmException {
        byte[] hash = hash(algorithm, data);
        return bytesToString(hash);
    }

    private static String bytesToString(byte[] data) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] temp = new char[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            byte b = data[i];
            temp[i * 2] = hexDigits[b >>> 4 & 0x0f];
            temp[i * 2 + 1] = hexDigits[b & 0x0f];
        }
        return new String(temp);
    }
}
