package simulationconsole.entanglement;

import com.hwaipy.science.polarizationcontrol.m1.M1Process;
import com.hwaipy.science.polarizationcontrol.m1.M1ProcessException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hwaipy
 */
public class EntanglementSimulationConsole {

    private static Random random = null;
    private static FiberTransform ft1;
    private static FiberTransform ft2;
    private static final WavePlate qwp11 = QuarterWavePlate.create(0);
    private static final WavePlate qwp12 = QuarterWavePlate.create(0);
    private static final WavePlate hwp1 = HalfWavePlate.create(0);
    private static final WavePlate qwp21 = QuarterWavePlate.create(0);
    private static final WavePlate qwp22 = QuarterWavePlate.create(0);
    private static final WavePlate hwp2 = HalfWavePlate.create(0);
    private static double noise = 0;
    private static double brightness = 10000;
    private static boolean debug = false;

    public static void main(String[] args) throws IOException {
        LinkedList<String> paras = new LinkedList<>(Arrays.asList(args));
        Iterator<String> iterator = paras.iterator();
        while (iterator.hasNext()) {
            String para = iterator.next();
            switch (para) {
                case "-r":
                    String seedString = iterator.next();
                    long seed = Long.parseLong(seedString);
                    random = new Random(seed);
                    debug("Seeded: " + seed + System.lineSeparator());
                    break;
                case "-d":
                    debug = true;
                    break;
            }
        }
        if (random == null) {
            random = new Random();
            debug("Random" + System.lineSeparator());
        }
        ft1 = FiberTransform.createRandomFiber(random);
        ft2 = FiberTransform.createRandomFiber(random);
        ServerSocket serverSocket = new ServerSocket(54321);
        while (true) {
            final Socket socket = serverSocket.accept();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        serve(socket);
                    } catch (IOException ex) {
                        Logger.getLogger(EntanglementSimulationConsole.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }).start();
        }
    }

    public static void serve(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        doReset();
        debug("New Process. Reseted." + System.lineSeparator());
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            } else {
                try {
                    if (line.startsWith("set")) {
                        //[input]set #group(1~2) #number(1~3) #angle
                        //[output]ok
                        String[] splits = line.split(" ");
                        int group = Integer.parseInt(splits[1]);
                        int number = Integer.parseInt(splits[2]);
                        double angle = Double.parseDouble(splits[3]);
                        set(group, number, angle, printWriter);
                        debug("Set " + group + " " + number + " " + angle + System.lineSeparator());
                    } else if (line.equals("read")) {
                        //[input]read
                        //[output]coincidents(HH HV HD HA VH VV VD VA DH DV DD DA AH AV AD AA)
                        read(printWriter);
                    } else if (line.equals("reset")) {
                        //[input]reset
                        //[output]ok
                        reset(printWriter);
                        debug("Reset" + System.lineSeparator());
                    } else if (line.startsWith("noise")) {
                        //[input]noise #value
                        //[output]ok
                        String[] splits = line.split(" ");
                        double noiseValue = Double.parseDouble(splits[1]);
                        setNoise(noiseValue, printWriter);
                        debug("Noise set to " + noiseValue + System.lineSeparator());
                    } else if (line.startsWith("brightness")) {
                        //[input]brightness #value
                        //[output]ok
                        String[] splits = line.split(" ");
                        double brightnessValue = Double.parseDouble(splits[1]);
                        setBrightness(brightnessValue, printWriter);
                        debug("Brightness set to " + brightnessValue + System.lineSeparator());
                    } else {
                        printWriter.println("Unkonwn");
                        printWriter.flush();
                        debug("Unknown" + System.lineSeparator());
                    }
                } catch (Exception e) {
                    printWriter.println("Error");
                    printWriter.flush();
                    System.out.println("Error");
                    e.printStackTrace(System.out);
                }
            }
        }
    }

    private static void set(int group, int number, double angle, PrintWriter printWriter) {
        WavePlate wavePlate;
        switch (group * 3 + number) {
            case 4:
                wavePlate = qwp11;
                break;
            case 5:
                wavePlate = qwp12;
                break;
            case 6:
                wavePlate = hwp1;
                break;
            case 7:
                wavePlate = qwp21;
                break;
            case 8:
                wavePlate = qwp22;
                break;
            case 9:
                wavePlate = hwp2;
                break;
            default:
                throw new RuntimeException();
        }
        wavePlate.setTheta(angle / 180 * Math.PI);
        printWriter.println("OK");
        printWriter.flush();
    }

    private static void setNoise(double noiseValue, PrintWriter printWriter) {
        noise = noiseValue;
        printWriter.println("OK");
        printWriter.flush();
    }

    private static void setBrightness(double brightnessValue, PrintWriter printWriter) {
        brightness = brightnessValue;
        printWriter.println("OK");
        printWriter.flush();
    }

    private static void read(PrintWriter printWriter) {
        TwoPhotonState entanglement = new TwoPhotonState(noise);
        entanglement = entanglement.evolution(ft1, SinglePartyOperator.I)
                .evolution(SinglePartyOperator.I, ft2)
                .evolution(qwp11, SinglePartyOperator.I)
                .evolution(qwp12, SinglePartyOperator.I)
                .evolution(hwp1, SinglePartyOperator.I)
                .evolution(SinglePartyOperator.I, qwp21)
                .evolution(SinglePartyOperator.I, qwp22)
                .evolution(SinglePartyOperator.I, hwp2);
        double[][] coincidents = entanglement.coincidents();
        for (double[] cs : coincidents) {
            for (double c : cs) {
                printWriter.print((int) (c * brightness) + " ");
                debug((int) (c * brightness) + " ");
            }
        }
        printWriter.println();
        printWriter.flush();
        debug(System.lineSeparator());
    }

    private static void reset(PrintWriter printWriter) {
        doReset();
        printWriter.println("OK");
        printWriter.flush();
    }

    private static void doReset() {
        ft1 = FiberTransform.createRandomFiber(random);
        ft2 = FiberTransform.createRandomFiber(random);
        qwp11.setTheta(random.nextDouble() * Math.PI);
        qwp12.setTheta(random.nextDouble() * Math.PI);
        hwp1.setTheta(random.nextDouble() * Math.PI);
        qwp21.setTheta(random.nextDouble() * Math.PI);
        qwp22.setTheta(random.nextDouble() * Math.PI);
        hwp2.setTheta(random.nextDouble() * Math.PI);
//        showResult();
    }

    private static void debug(String output) {
        if (debug) {
            System.out.print(output);
        }
    }

    private static void showResult() {
        TwoPhotonState entanglement = new TwoPhotonState();
        entanglement = entanglement.evolution(ft1, SinglePartyOperator.I)
                .evolution(SinglePartyOperator.I, ft2)
                .evolution(qwp11, SinglePartyOperator.I)
                .evolution(qwp12, SinglePartyOperator.I)
                .evolution(hwp1, SinglePartyOperator.I);

        WavePlate qwp1 = QuarterWavePlate.create(0);
        WavePlate qwp2 = QuarterWavePlate.create(0);
        WavePlate hwp = HalfWavePlate.create(0);

        TwoPhotonState measurement1 = entanglement
                .evolution(SinglePartyOperator.I, qwp1)
                .evolution(SinglePartyOperator.I, qwp2)
                .evolution(SinglePartyOperator.I, hwp);
        double[][] coincidents1 = measurement1.coincidents();
        double cHH = coincidents1[1][0];
        double cHV = coincidents1[1][1];
        double cHD = coincidents1[1][2];
        double cHA = coincidents1[1][3];
        double cDH = coincidents1[3][0];
        double cDV = coincidents1[3][1];
        double cDD = coincidents1[3][2];
        double cDA = coincidents1[3][3];

        qwp2.increase(-Math.PI / 4);
        hwp.increase(-Math.PI / 8);

        TwoPhotonState measurement2 = entanglement
                .evolution(SinglePartyOperator.I, qwp1)
                .evolution(SinglePartyOperator.I, qwp2)
                .evolution(SinglePartyOperator.I, hwp);
        double[][] coincidents2 = measurement2.coincidents();
        double cHL = coincidents2[1][0];
        double cHR = coincidents2[1][1];
        double cDL = coincidents2[3][0];
        double cDR = coincidents2[3][1];

        System.out.println(cHH + " " + cHV + " " + cHD + " " + cHA + " " + cHL + " " + cHR + " "
                + cDH + " " + cDV + " " + cDD + " " + cDA + " " + cDL + " " + cDR);

        M1Process m1Process = null;
        try {
            m1Process = M1Process.calculate(new double[]{cHH, cHV, cHD, cHA, cHL, cHR, cDH, cDV, cDD, cDA, cDL, cDR});
        } catch (M1ProcessException ex) {
            ex.printStackTrace(System.out);
        }
        if (m1Process != null) {
            double[] result = m1Process.getResults();
            qwp1.setTheta(result[0]);
            qwp2.setTheta(result[1]);
            hwp.setTheta(result[2]);
            System.out.println(Arrays.toString(result));
            TwoPhotonState measurementFinal = entanglement
                    .evolution(SinglePartyOperator.I, qwp1)
                    .evolution(SinglePartyOperator.I, qwp2)
                    .evolution(SinglePartyOperator.I, hwp);
            double cH = measurementFinal.contrastHV();
            double cD = measurementFinal.contrastDA();
            System.out.println(cH);
            System.out.println(cD);
        }
    }
}
