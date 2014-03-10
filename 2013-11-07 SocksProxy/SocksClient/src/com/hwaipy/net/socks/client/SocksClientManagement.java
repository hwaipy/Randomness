package com.hwaipy.net.socks.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 *
 * @author Hwaipy
 */
public class SocksClientManagement {

    public void startSocksService(int port) throws IOException {
        NioSocketAcceptor acceptor = new NioSocketAcceptor();
        acceptor.setReuseAddress(true);
        acceptor.setHandler(new SocksClientServiceHandler());
        acceptor.bind(new InetSocketAddress(1080));
    }
}
