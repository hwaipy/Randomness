package com.hwaipy.apple.iso.backup;

/**
 *
 * @author Hwaipy
 */
public class Call extends Message {

    private int duration;
    private int flag;

    public int getDuration() {
        return duration;
    }

    void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    void setDirection(Direction direction) {
    }

    @Override
    public Direction getDirection() {
        return getFlag() % 2 == 0 ? Direction.INCOMING : Direction.OUTGOING;
    }

    public int getFlag() {
        return flag;
    }

    void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String getContext() {
        StringBuilder sb = new StringBuilder();
        sb.append(getDuration()).append("s (").append(DurationFormat.format(duration))
                .append(")").append(System.lineSeparator()).append(getAddress())
                .append("(").append(getFlagDescription()).append(")");
        return sb.toString();
    }

    @Override
    void setContext(String context) {
    }

    @Override
    public String getSubject() {
        return "与 " + getDisplayName() + " 的通话记录";
    }

    @Override
    public void setSubject(String subject) {
    }

    private String getFlagDescription() {
        boolean incoming = getFlag() % 2 == 0;
        boolean available = getDuration() > 0;
        String desc;
        if (incoming) {
            if (available) {
                desc = "已接来电";
            } else {
                desc = "未接来电";
            }
        } else {
            if (available) {
                desc = "已拨电话";
            } else {
                desc = "已拨电话";
            }
        }
        return desc + "," + flag;
    }
}
