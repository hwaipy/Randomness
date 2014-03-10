package simulationconsole;

import com.hwaipy.science.polarizationcontrol.device.FiberTransform;
import com.hwaipy.science.polarizationcontrol.device.Polarization;
import com.hwaipy.science.polarizationcontrol.device.WavePlate;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hwaipy
 */
public class SimulationSocket {

    private static FiberTransform ft = FiberTransform.createRandomFiber();
    private static final WavePlate qwp1 = new WavePlate(Math.PI / 2, 0);
    private static final WavePlate qwp2 = new WavePlate(Math.PI / 2, 0);
    private static final WavePlate hwp = new WavePlate(Math.PI, 0);
    private static int laser = 0;//1 for H, 2 for D

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(54321);
        while (true) {
            final Socket socket = serverSocket.accept();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        serve(socket);
                    } catch (IOException ex) {
                        Logger.getLogger(SimulationSocket.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }).start();
        }
    }

    public static void serve(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            } else {
                try {
                    if (line.equals("open H")) {
                        openLaser(1, printWriter);
                    } else if (line.equals("open D")) {
                        openLaser(2, printWriter);
                    } else if (line.equals("close H") || line.equals("close D")) {
                        closeLaser(printWriter);
                    } else if (line.startsWith("set")) {
                        String[] splits = line.split(" ");
                        int number = Integer.parseInt(splits[1]);
                        double angle = Double.parseDouble(splits[2]);
                        set(number, angle, printWriter);
                    } else if (line.equals("read")) {
                        read(printWriter);
                    } else if (line.equals("reset")) {
                        reset(printWriter);
                    } else {
                        printWriter.println("Unkonwn");
                        printWriter.flush();
                    }
                } catch (NumberFormatException numberFormatException) {
                    numberFormatException.printStackTrace(System.err);
                }
            }
        }
    }

    private static void openLaser(int code, PrintWriter printWriter) {
        laser = code;
        printWriter.println("OK");
        printWriter.flush();
    }

    private static void closeLaser(PrintWriter printWriter) {
        laser = 0;
        printWriter.println("OK");
        printWriter.flush();
    }

    private static void set(int number, double angle, PrintWriter printWriter) {
        WavePlate wavePlate;
        switch (number) {
            case 1:
                wavePlate = qwp1;
                break;
            case 2:
                wavePlate = qwp2;
                break;
            case 3:
                wavePlate = hwp;
                break;
            default:
                throw new RuntimeException();
        }
        wavePlate.setTheta(angle / 180 * Math.PI);
        printWriter.println("OK");
        printWriter.flush();
    }

    private static void read(PrintWriter printWriter) {
        Polarization polarization;
        switch (laser) {
            case 0:
                printWriter.println("0.0 0.0 0.0 0.0");
                printWriter.flush();
                return;
            case 1:
                polarization = Polarization.H;
                break;
            case 2:
                polarization = Polarization.D;
                break;
            default:
                throw new RuntimeException();
        }
        Polarization p = polarization.transform(ft).transform(qwp1).transform(qwp2).transform(hwp);
        double H = p.getH();
        double V = p.getV();
        double D = p.getD();
        double A = p.getA();
        DecimalFormat format = new DecimalFormat("0.0000");
        printWriter.println(format.format(H) + " " + format.format(V) + " " + format.format(D) + " " + format.format(A));
        printWriter.flush();
    }

    private static void reset(PrintWriter printWriter) {
        ft = FiberTransform.createRandomFiber();
        printWriter.println("OK");
        printWriter.flush();
    }
}
