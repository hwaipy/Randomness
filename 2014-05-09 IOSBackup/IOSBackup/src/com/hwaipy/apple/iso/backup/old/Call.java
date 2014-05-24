package com.hwaipy.apple.iso.backup.old;

import com.hwaipy.apple.iso.backup.Message;

/**
 *
 * @author Hwaipy
 */
public class Call extends Message {

    private int duration;

    public int getDuration() {
        return (Integer) getContent();
    }

    void setDuration(int duration) {
        setContent(duration);
    }

//    @Override
//    public String getContext() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(getDuration()).append("s (").append(DurationFormat.format(duration))
//                .append(")").append(System.lineSeparator()).append(getAddress())
//                .append("(").append(getFlagDescription()).append(")");
//        return sb.toString();
//    }
//
//    @Override
//    void setContext(String context) {
//    }
//    @Override
//    public String getSubject() {
//        return "与 " + getDisplayName() + " 的通话记录";
//    }
//    @Override
//    public void setSubject(String subject) {
//    }
    private String getFlagDescription() {
        boolean available = getDuration() > 0;
        String desc;
        if (isFromMe()) {
            if (available) {
                desc = "已拨电话";
            } else {
                desc = "已拨电话";
            }
        } else {
            if (available) {
                desc = "已接来电";
            } else {
                desc = "未接来电";
            }
        }
        return desc + "," + flag;
    }
}
