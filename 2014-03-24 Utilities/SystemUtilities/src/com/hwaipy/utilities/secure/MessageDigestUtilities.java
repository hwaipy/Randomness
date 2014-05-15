package com.hwaipy.utilities.secure;

import com.hwaipy.utilities.format.NumberFormatUtilities;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Hwaipy
 */
public class MessageDigestUtilities {

    /**
     * 为data生成特定的hash.
     *
     * @param algorithm
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] hash(String algorithm, byte[] data) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        byte[] digest = messageDigest.digest(data);
        return digest;
    }

    /**
     * 为data生成特定的hash，并以16进制形式返回.
     *
     * @param algorithm
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String hashToString(String algorithm, byte[] data) throws NoSuchAlgorithmException {
        byte[] result = hash(algorithm, data);
        return NumberFormatUtilities.toHex(result);
    }
}
