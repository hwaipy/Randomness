package com.hwaipy.unifieddeviceinterface.datadispatch;

import java.util.EventListener;

/**
 * The listener interface for receiving data from devices.mouse events (press,
 * release, click, enter, and exit) on a component. (To track mouse moves and
 * mouse drags, use the <code>MouseMotionListener</code>.)
 * <P>
 * The class that is interested in processing a mouse event either implements
 * this interface (and all the methods it contains) or extends the abstract
 * <code>MouseAdapter</code> class (overriding only the methods of interest).
 * <P>
 * The listener object created from that class is then registered with a
 * component using the component's <code>addMouseListener</code> method. A mouse
 * event is generated when the mouse is pressed, released clicked (pressed and
 * released). A mouse event is also generated when the mouse cursor enters or
 * leaves a component. When a mouse event occurs, the relevant method in the
 * listener object is invoked, and the <code>MouseEvent</code> is passed to it.
 * TODOC
 *
 * @author Hwaipy
 * @param <DATA_TYPE>
 */
public interface DataIncomeListener<DATA_TYPE> extends EventListener {

    public void dataIncome(DataIncomeEvent<DATA_TYPE> event);
}
