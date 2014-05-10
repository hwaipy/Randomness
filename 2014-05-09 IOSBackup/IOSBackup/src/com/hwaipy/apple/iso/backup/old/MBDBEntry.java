package com.hwaipy.apple.iso.backup.old;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Hwaipy
 */
public class MBDBEntry {

    private String domain;
    private String path;
    private String linkTarget;
    private String dataHash;
    private String unknown1;
    private int mode;
    private long unknown2;
    private long unknown3;
    private long userID;
    private long groupID;
    private long time1;
    private long time2;
    private long time3;
    private long fileLength;
    private int flag;
    private String hash;
    private final LinkedHashMap<String, String> properties = new LinkedHashMap<>();

    public String getDomain() {
        return domain;
    }

    void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    void setPath(String path) {
        this.path = path;
    }

    public String getLinkTarget() {
        return linkTarget;
    }

    void setLinkTarget(String linkTarget) {
        this.linkTarget = linkTarget;
    }

    public String getDataHash() {
        return dataHash;
    }

    void setDataHash(String dataHash) {
        this.dataHash = dataHash;
    }

    public String getUnknown1() {
        return unknown1;
    }

    void setUnknown1(String unknown1) {
        this.unknown1 = unknown1;
    }

    public int getMode() {
        return mode;
    }

    void setMode(int mode) {
        this.mode = mode;
    }

    public long getUnknown2() {
        return unknown2;
    }

    void setUnknown2(long unknown2) {
        this.unknown2 = unknown2;
    }

    public long getUnknown3() {
        return unknown3;
    }

    void setUnknown3(long unknown3) {
        this.unknown3 = unknown3;
    }

    public long getUserID() {
        return userID;
    }

    void setUserID(long userID) {
        this.userID = userID;
    }

    public long getGroupID() {
        return groupID;
    }

    void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    public long getTime1() {
        return time1;
    }

    void setTime1(long time1) {
        this.time1 = time1;
    }

    public long getTime2() {
        return time2;
    }

    void setTime2(long time2) {
        this.time2 = time2;
    }

    public long getTime3() {
        return time3;
    }

    void setTime3(long time3) {
        this.time3 = time3;
    }

    public long getFileLength() {
        return fileLength;
    }

    void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public int getFlag() {
        return flag;
    }

    void setFlag(int flag) {
        this.flag = flag;
    }

    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    public void addProperties(String key, String value) {
        properties.put(key, value);
    }

    public String getHash() {
        if (hash == null) {
            try {
                hash = MessageDigestUtilities.hashForString("SHA1", (domain + "-" + path).getBytes("UTF-8"));
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
        }
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Domain: ").append(domain).append(System.lineSeparator())
                .append("Path: ").append(path).append(System.lineSeparator())
                .append("LinkTarget: ").append(linkTarget).append(System.lineSeparator())
                .append("DataHash: ").append(dataHash).append(System.lineSeparator())
                .append("Unknown: ").append(unknown1).append(System.lineSeparator())
                .append("Mode: ").append(mode).append(System.lineSeparator())
                .append("Unknown: ").append(unknown2).append(System.lineSeparator())
                .append("Unknown: ").append(unknown3).append(System.lineSeparator())
                .append("UserID: ").append(userID).append(System.lineSeparator())
                .append("GroupID: ").append(groupID).append(System.lineSeparator())
                .append("Time1: ").append(time1).append(System.lineSeparator())
                .append("Time2: ").append(time2).append(System.lineSeparator())
                .append("Time3: ").append(time3).append(System.lineSeparator())
                .append("FileLength: ").append(fileLength).append(System.lineSeparator())
                .append("Hash: ").append(getHash()).append(System.lineSeparator())
                .append("Flag: ").append(flag).append(System.lineSeparator());
        if (!properties.isEmpty()) {
            sb.append("Properties: ").append(System.lineSeparator());
            properties.entrySet().stream().forEach((property) -> {
                sb.append(property.getKey()).append("\t").
                        append(property.getValue()).append(System.lineSeparator());
            });
        }
        return sb.toString();
    }
    public static final String HASH_SMS = "3d0d7e5fb2ce288813306e4d4636395e047a3d28";
    public static final String HASH_ADDRES_BOOK = "31bb7ba8914766d4ba40d6dfb6113c8b614be442";
    public static final String HASH_CALL_HISTORY = "2b2b0084a1bc3a5ac8c27afdf14afb42c61a19ca";
}
