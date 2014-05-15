package com.hwaipy.apple.iso.backup.old;

import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Hwaipy
 */
public class MessageDigestUtilities {

    public static String hashForString(String algorithm, byte[] data) throws NoSuchAlgorithmException {
        byte[] hash = hash(algorithm, data);
        return bytesToString(hash);
    }
}
