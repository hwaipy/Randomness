package com.hwaipy.device.advantech.jna;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author Administrator
 */
public class ExceptionCallbacker implements ExceptionCallback {

    private final LinkedList<String> exceptions = new LinkedList<>();

    @Override
    public void exception(String info) {
        exceptions.add(info);
    }

    public Collection<String> getExceptions() {
        return Collections.unmodifiableCollection(exceptions);
    }

    public String getException() {
        StringBuilder sb = new StringBuilder();
        for (String ex : exceptions) {
            sb.append(ex);
            if (!ex.endsWith("\n")) {
                sb.append("\n");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
