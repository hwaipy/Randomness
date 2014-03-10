package com.hwaipy.net.socks.client;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author Hwaipy
 */
public class SocksClientServiceHandler implements IoHandler {

    @Override
    public void sessionCreated(IoSession session) throws Exception {
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        session.setAttribute(SocksConnection.class, new SocksConnection());
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        System.out.println("exception");
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        System.out.println(message);
        SocksConnection socksConnection = (SocksConnection) session.getAttribute(SocksConnection.class);
        socksConnection
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
    }
}
