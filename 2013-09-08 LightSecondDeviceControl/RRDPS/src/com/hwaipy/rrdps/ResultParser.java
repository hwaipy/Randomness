package com.hwaipy.rrdps;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Hwaipy
 */
public class ResultParser {

    private final ArrayList<Decoder.Entry> result;
    private int encode0 = 0;
    private int encode1 = 0;
    private int encode0Correct = 0;
    private int encode1Correct = 0;
    private int apd0 = 0;
    private int apd1 = 0;
    private int apd0Correct = 0;
    private int apd1Correct = 0;
    private final int upBound;

    public ResultParser(ArrayList<Decoder.Entry> result) {
        this(result, 128);
    }

    public ResultParser(ArrayList<Decoder.Entry> result, int upBound) {
        this.result = result;
        this.upBound = upBound;
        parse();
    }

    public int getEncode0Count() {
        return encode0;
    }

    public int getEncode1Count() {
        return encode1;
    }

    public double getEncode0ErrorRate() {
        return (encode0 - encode0Correct) / 1. / encode0;
    }

    public double getEncode1ErrorRate() {
        return (encode1 - encode1Correct) / 1. / encode1;
    }

    public int getApd0Count() {
        return apd0;
    }

    public int getApd1Count() {
        return apd1;
    }

    public double getApd0ErrorRate() {
        return (apd0 - apd0Correct) / 1. / apd0;
    }

    public double getApd1ErrorRate() {
        return (apd1 - apd1Correct) / 1. / apd1;
    }

    private void parse() {
        Iterator<Decoder.Entry> iterator = result.iterator();
        while (iterator.hasNext()) {
            Decoder.Entry decoder = iterator.next();
            if (decoder.getPulseIndex() >= upBound) {
                continue;
            }
            if (decoder.getDecodingRandom().getRandom() >= upBound) {
                continue;
            }
            if (decoder.getEncode() == 0) {
                encode0++;
                if (decoder.isCorrect()) {
                    encode0Correct++;
                }
            } else {
                encode1++;
                if (decoder.isCorrect()) {
                    encode1Correct++;
                }
            }
            if (decoder.getDecode() == 0) {
                apd0++;
                if (decoder.isCorrect()) {
                    apd0Correct++;
                }
            } else {
                apd1++;
                if (decoder.isCorrect()) {
                    apd1Correct++;
                }
            }
        }
    }

    public void resultByBobQRNG(ArrayList<Decoder.Entry> result) {
        int[] correctList = new int[128];
        int[] errorList = new int[128];
        result.stream().forEach((r) -> {
            int random = r.getDecodingRandom().getRandom();
            if (r.isCorrect()) {
                correctList[random]++;
            } else {
                errorList[random]++;
            }
        });
        for (int i = 0; i < 128; i++) {
//            if ((i % 2) == 0) {
            System.out.println(i + "\t" + correctList[i] + "\t" + errorList[i] + "\t" + (((double) correctList[i]) / errorList[i]));
//            } else {
//                System.out.println(i + "\t" + correctList[i] + "\t" + errorList[i] + "\t" + (1 / (((double) correctList[i]) / errorList[i])));
//            }
        }
    }

    public void resultByBobQRNG12(ArrayList<Decoder.Entry> result) {
        int[] correct0List = new int[128];
        int[] correct1List = new int[128];
        int[] error0List = new int[128];
        int[] error1List = new int[128];
        result.stream().forEach((r) -> {
            int random = r.getDecodingRandom().getRandom();
            int encode = r.getEncode();
            if (encode == 0) {
                if (r.isCorrect()) {
                    correct0List[random]++;
                } else {
                    error0List[random]++;
                }
            } else {
                if (r.isCorrect()) {
                    correct1List[random]++;
                } else {
                    error1List[random]++;
                }
            }
        });
        for (int i = 0; i < 128; i++) {
//            if ((i % 2) == 0) {
            System.out.println(i + "\t" + correct0List[i] + "\t" + error0List[i] + "\t" + (((double) correct0List[i]) / error0List[i]) + "\t" + correct1List[i] + "\t" + error1List[i] + "\t" + (((double) correct1List[i]) / error1List[i]));
//            } else {
//                System.out.println(i + "\t" + correctList[i] + "\t" + errorList[i] + "\t" + (1 / (((double) correctList[i]) / errorList[i])));
//            }
        }
    }

    public void ResultOutFile(ArrayList<Decoder.Entry> result) throws IOException {
        int ErrorCount = 0;
        int RightCodeCount = 0;
        int ErrorCount0 = 0;
        int ErrorCount1 = 0;
        int CodeCount0 = 0;
        int CodeCount1 = 0;
        float Ratio = 0, APD1Ratio = 0, APD2Ratio = 0;
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getEncode() == 0) {

                if (result.get(i).getDecode() == 0) {
                    CodeCount0++;
                } else {
                    ErrorCount1++;
                }
            } else if (result.get(i).getEncode() == 1) {
                if (result.get(i).getDecode() == 1) {
                    CodeCount1++;
                } else {
                    ErrorCount0++;
                }
            }
        }
        APD1Ratio = (float) CodeCount0 / (float) ErrorCount1;
        APD2Ratio = (float) CodeCount1 / (float) ErrorCount0;
        RightCodeCount = CodeCount0 + CodeCount1;
        ErrorCount = ErrorCount0 + ErrorCount1;
        Ratio = (float) RightCodeCount / (float) ErrorCount;

        System.out.println("APD1 Code 0:\t" + CodeCount0 + "\t Error Code 1:\t" + ErrorCount1 + "\t APD1 Ratio:\t" + APD1Ratio);
        System.out.println("APD2 Code 1:\t" + CodeCount1 + "\t Error Code 0:\t" + ErrorCount0 + "\t APD2 Ratio:\t" + APD2Ratio);
        System.out.println("Total Right Code:\t" + RightCodeCount + "\t Total Error Code :\t" + ErrorCount + "\t Ratio:\t" + Ratio);
    }

    public void ResultStatistics(ArrayList<Decoder.Entry> result, String id) throws IOException {

        int ErrorCount0 = 0;
        int ErrorCount1 = 0;
        int CodeCount0 = 0;
        int CodeCount1 = 0;
        float APD1Ratio = 0, APD2Ratio = 0;
        int Second = 1;
        File codewrite = new File("G:\\DPS数据处理\\DPS实验数据\\数据处理TXT\\" + id + "每秒统计" + ".txt"); // 相对路径，如果没有则要建立一个新的output。txt文件  
        codewrite.createNewFile(); // 创建新文件  
        BufferedWriter code = new BufferedWriter(new FileWriter(codewrite));
        code.write("Second\t" + "APD1 Code 0\t" + "Error Code 1\t" + "APD1 Ratio\t"
                + "APD2 Code 1\t" + "Error Code 0\t" + "APD2 Ratio\t" + "\r\n");
        System.out.println("Second\t" + "APD1 Code 0\t" + "Error Code 1\t" + "APD1 Ratio\t"
                + "APD2 Code 1\t" + "Error Code 0\t" + "APD2 Ratio\t" + "\r\n");
        for (int i = 0; i < result.size(); i++) {
            //TODO 时间信息需要补上
            if (Second > (int) (result.get(i).getAPDTime() / 1000000000000l)) {

                if (result.get(i).getEncode() == 0) {

                    if (result.get(i).getDecode() == 0) {
                        CodeCount0++;

                    } else {
                        ErrorCount1++;
                    }
                } else if (result.get(i).getEncode() == 1) {
                    if (result.get(i).getDecode() == 1) {
                        CodeCount1++;
                    } else {
                        ErrorCount0++;
                    }
                }
            } else {
                APD1Ratio = (float) CodeCount0 / (float) ErrorCount1;
                APD2Ratio = (float) CodeCount1 / (float) ErrorCount0;
                code.write((Second - 1) + "\t" + CodeCount0 + "\t" + ErrorCount1 + "\t " + APD1Ratio
                        + "\t " + CodeCount1 + "\t " + ErrorCount0 + "\t " + APD2Ratio + "\r\n");

                System.out.println((Second - 1) + "\t" + CodeCount0 + "\t" + ErrorCount1 + "\t " + APD1Ratio
                        + "\t " + CodeCount1 + "\t " + ErrorCount0 + "\t " + APD2Ratio + "\r\n");
                APD1Ratio = 0;
                APD2Ratio = 0;
                CodeCount0 = 0;
                CodeCount1 = 0;
                ErrorCount0 = 0;
                ErrorCount1 = 0;
                Second = (int) (result.get(i).getAPDTime() / 1000000000000l) + 1;
            }

        }
        code.flush();
        code.close();
    }

    public void ResultPrint(ArrayList<Decoder.Entry> result) {
        int ErrorCount = 0;
        int RightCodeCount = 0;
        int ErrorCount0 = 0;
        int ErrorCount1 = 0;
        int CodeCount0 = 0;
        int CodeCount1 = 0;
        float Ratio = 0, APD1Ratio = 0, APD2Ratio = 0;
        System.out.println("Code Count: " + result.size() + "\r\n");
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getEncode() == 0) {

                if (result.get(i).getDecode() == 0) {
                    CodeCount0++;
                    System.out.println("Round: \t" + result.get(i).getRoundIndex() + "\t PulseIndex: \t" + result.get(i).getPulseIndex()
                            + "\t Encode: \t" + result.get(i).getEncode() + "\t Decode: \t" + result.get(i).getDecode());
                } else {
                    ErrorCount1++;
                    System.out.println("Round: \t" + result.get(i).getRoundIndex() + "\t PulseIndex: \t" + result.get(i).getPulseIndex()
                            + "\t Encode: \t" + result.get(i).getEncode() + "\t Decode: \t" + result.get(i).getDecode() + "\t Error Code!");
                }
            } else if (result.get(i).getEncode() == 1) {
                if (result.get(i).getDecode() == 1) {
                    CodeCount1++;
                    System.out.println("Round: \t" + result.get(i).getRoundIndex() + "\t PulseIndex: \t" + result.get(i).getPulseIndex()
                            + "\t Encode: \t" + result.get(i).getEncode() + "\t Decode: \t" + result.get(i).getDecode());
                } else {
                    ErrorCount0++;
                    System.out.println("Round: \t" + result.get(i).getRoundIndex() + "\t PulseIndex: \t" + result.get(i).getPulseIndex()
                            + "\t Encode: \t" + result.get(i).getEncode() + "\t Decode: \t" + result.get(i).getDecode() + "\t Error Code!");
                }
            }
        }
        APD1Ratio = CodeCount0 / ErrorCount1;
        APD2Ratio = CodeCount1 / ErrorCount0;
        RightCodeCount = CodeCount0 + CodeCount1;
        ErrorCount = ErrorCount0 + ErrorCount1;
        Ratio = (float) RightCodeCount / (float) ErrorCount;
        System.out.println("APD1 Code 0:\t" + CodeCount0 + "\t Error Code 1:\t" + ErrorCount1 + "\t APD1 Ratio:\t" + APD1Ratio);
        System.out.println("APD2 Code 1:\t" + CodeCount1 + "\t Error Code 0:\t" + ErrorCount0 + "\t APD2 Ratio:\t" + APD2Ratio);
        System.out.println("Total Right Code:\t" + RightCodeCount + "\t Total Error Code :\t" + ErrorCount + "\t Ratio:\t" + Ratio);
    }
}
