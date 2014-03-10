package com.hwaipy.test;

import com.hwaipy.net.socks.client.SocksClientManagement;
import java.io.IOException;

/**
 *
 * @author Hwaipy
 */
public class MinaTest {

    public static void main(String[] args) throws IOException {
        new SocksClientManagement().startSocksService(1080);
    }
}
