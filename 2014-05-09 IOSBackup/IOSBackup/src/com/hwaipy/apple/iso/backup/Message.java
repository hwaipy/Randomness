package com.hwaipy.apple.iso.backup;

import java.io.File;
import java.util.Arrays;

/**
 *
 * @author HwaipyLab
 */
public abstract class Message implements Comparable<Message> {

    private String name;
    private String address;
    private Direction direction;
    private long date;
    private String context;
    private String subject;
    private File[] attachments;

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        if (name != null && !name.isEmpty()) {
            return name;
        } else {
            return address;
        }
    }

    public String getAddress() {
        return (address == null || "".equals(address)) ? "Unknown" : address;
    }

    void setAddress(String address) {
        this.address = address;
    }

    public Direction getDirection() {
        return direction;
    }

    void setDirection(Direction direction) {
        this.direction = direction;
    }

    public long getDate() {
        return date;
    }

    void setDate(long date) {
        this.date = date;
    }

    public String getContext() {
        return context;
    }

    void setContext(String context) {
        this.context = context;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public File[] getAttachments() {
        return Arrays.copyOf(attachments, attachments.length);
    }

    void setAttachments(File[] attachments) {
        this.attachments = Arrays.copyOf(attachments, attachments.length);
    }
// a -1,0,+1
//     *          is less than, equal to, or greater than the specified object.

    @Override
    public int compareTo(Message o) {
        if (o == null) {
            return 1;
        }
        long od = o.getDate();
        long d = getDate();
        if (d < od) {
            return -1;
        }
        if (d > od) {
            return 1;
        }
        return 0;
    }
}
