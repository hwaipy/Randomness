/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hwaipy.device.advantech;

/**
 *
 * @author Hwaipy Lab
 */
class Util {

    static String createStringFromBytesArray(byte[] bytes) {
        int length = 0;
        for (; length < bytes.length; length++) {
            if (bytes[length] == 0) {
                break;
            }
        }
        return new String(bytes, 0, length);
    }
}
