package com.hwaipy.device.advantech.jna;

import com.sun.jna.Callback;

/**
 *
 * @author Administrator
 */
public interface ExceptionCallback extends Callback {

    public void exception(String info);
}
