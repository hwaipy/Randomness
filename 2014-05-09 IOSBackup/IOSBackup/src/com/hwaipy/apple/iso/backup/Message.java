package com.hwaipy.apple.iso.backup;

/**
 *
 * @author HwaipyLab
 */
public abstract class Message implements Comparable<Message> {

    private String name;
    private String address;
    private boolean isFromMe;
    private long date;
    private Object content;

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

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isFromMe() {
        return isFromMe;
    }

    public void setIsFromMe(boolean isFromMe) {
        this.isFromMe = isFromMe;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

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
