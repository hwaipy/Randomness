package com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data;

/**
 *
 * @author Hwaipy
 */
public class Description {

    private String ident;
    private String formatVersion;
    private String creatorName;
    private String creatorVersion;
    private String fileTime;
    private final byte[] CR_LF = new byte[2];
    private String comment;
    private int numberOfCurves;
    private int bitsPerRecord;
    private int activeCurve;
    private int measurementMode;
    private int subMode;
    private int binning;
    private double resolution;
    private int offset;
    private int acquisitionTime;
    private int stopAt;
    private int stopOnOvfl;
    private int restart;
    private int displayLinLog;
    private int displayTimeAxisFrom;
    private int displayTimeAxisTo;
    private int displayCountAxisFrom;
    private int displayCountAxisTo;

    public String getIdent() {
        return ident;
    }

    void setIdent(String ident) {
        this.ident = ident;
    }

    public String getFormatVersion() {
        return formatVersion;
    }

    void setFormatVersion(String formatVersion) {
        this.formatVersion = formatVersion;
    }

    public String getCreatorName() {
        return creatorName;
    }

    void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorVersion() {
        return creatorVersion;
    }

    void setCreatorVersion(String creatorVersion) {
        this.creatorVersion = creatorVersion;
    }

    public String getFileTime() {
        return fileTime;
    }

    void setFileTime(String fileTime) {
        this.fileTime = fileTime;
    }

    public byte[] getCR_LF() {
        byte[] bs = new byte[2];
        bs[0] = CR_LF[0];
        bs[1] = CR_LF[1];
        return bs;
    }

    void setCR_LF(byte b1, byte b2) {
        CR_LF[0] = b1;
        CR_LF[1] = b2;
    }

    public String getComment() {
        return comment;
    }

    void setComment(String comment) {
        this.comment = comment;
    }

    public int getNumberOfCurves() {
        return numberOfCurves;
    }

    void setNumberOfCurves(int numberOfCurves) {
        this.numberOfCurves = numberOfCurves;
    }

    public int getBitsPerRecord() {
        return bitsPerRecord;
    }

    void setBitsPerRecord(int bitsPerRecord) {
        this.bitsPerRecord = bitsPerRecord;
    }

    public int getActiveCurve() {
        return activeCurve;
    }

    void setActiveCurve(int activeCurve) {
        this.activeCurve = activeCurve;
    }

    public int getMeasurementMode() {
        return measurementMode;
    }

    void setMeasurementMode(int measurementMode) {
        this.measurementMode = measurementMode;
    }

    public int getSubMode() {
        return subMode;
    }

    void setSubMode(int subMode) {
        this.subMode = subMode;
    }

    public int getBinning() {
        return binning;
    }

    void setBinning(int binning) {
        this.binning = binning;
    }

    public double getResolution() {
        return resolution;
    }

    void setResolution(double resolution) {
        this.resolution = resolution;
    }

    public int getOffset() {
        return offset;
    }

    void setOffset(int offset) {
        this.offset = offset;
    }

    public int getAcquisitionTime() {
        return acquisitionTime;
    }

    void setAcquisitionTime(int acquisitionTime) {
        this.acquisitionTime = acquisitionTime;
    }

    public int getStopAt() {
        return stopAt;
    }

    void setStopAt(int stopAt) {
        this.stopAt = stopAt;
    }

    public int getStopOnOvfl() {
        return stopOnOvfl;
    }

    void setStopOnOvfl(int stopOnOvfl) {
        this.stopOnOvfl = stopOnOvfl;
    }

    public int getRestart() {
        return restart;
    }

    void setRestart(int restart) {
        this.restart = restart;
    }

    public int getDisplayLinLog() {
        return displayLinLog;
    }

    void setDisplayLinLog(int displayLinLog) {
        this.displayLinLog = displayLinLog;
    }

    public int getDisplayTimeAxisFrom() {
        return displayTimeAxisFrom;
    }

    void setDisplayTimeAxisFrom(int displayTimeAxisFrom) {
        this.displayTimeAxisFrom = displayTimeAxisFrom;
    }

    public int getDisplayTimeAxisTo() {
        return displayTimeAxisTo;
    }

    void setDisplayTimeAxisTo(int displayTimeAxisTo) {
        this.displayTimeAxisTo = displayTimeAxisTo;
    }

    public int getDisplayCountAxisFrom() {
        return displayCountAxisFrom;
    }

    void setDisplayCountAxisFrom(int displayCountAxisFrom) {
        this.displayCountAxisFrom = displayCountAxisFrom;
    }

    public int getDisplayCountAxisTo() {
        return displayCountAxisTo;
    }

    void setDisplayCountAxisTo(int displayCountAxisTo) {
        this.displayCountAxisTo = displayCountAxisTo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ident: ").append(ident).append("\r")
                .append("FormatVersion: ").append(formatVersion).append("\r")
                .append("CreatorName: ").append(creatorName).append("\r")
                .append("CreatorVersion: ").append(creatorVersion).append("\r")
                .append("FileTime: ").append(fileTime).append("\r")
                .append("CR/LF: ").append((int) CR_LF[0]).append(",").append((int) CR_LF[1]).append("\r")
                .append("Comment: ").append(comment).append("\r")
                .append("NumberOfCurves: ").append(numberOfCurves).append("\r")
                .append("BitsPerRecord: ").append(bitsPerRecord).append("\r")
                .append("ActiveCurve: ").append(activeCurve).append("\r")
                .append("MeasurementMode: ").append(measurementMode).append("\r")
                .append("SubMode: ").append(subMode).append("\r")
                .append("Binning: ").append(binning).append("\r")
                .append("Resolution: ").append(resolution).append("\r")
                .append("Offset: ").append(offset).append("\r")
                .append("AcquisitionTime: ").append(acquisitionTime).append("\r")
                .append("StopAt: ").append(stopAt).append("\r")
                .append("StopOnOvfl: ").append(stopOnOvfl).append("\r")
                .append("Restart: ").append(restart).append("\r")
                .append("DisplayLinLog: ").append(displayLinLog).append("\r")
                .append("DisplayTimeAxisFrom: ").append(displayTimeAxisFrom).append("\r")
                .append("DisplayTimeAxisTo: ").append(displayTimeAxisTo).append("\r")
                .append("DisplayCountAxisFrom: ").append(displayCountAxisFrom).append("\r")
                .append("DisplayCountAxisTo: ").append(displayCountAxisTo).append("\r");

        return sb.toString();
    }
}
