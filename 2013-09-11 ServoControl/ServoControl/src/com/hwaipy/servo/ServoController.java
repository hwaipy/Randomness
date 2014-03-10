package com.hwaipy.servo;

import com.hwaipy.device.advantech.AdvantechADC;
import com.hwaipy.device.advantech.Device;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Hwaipy Lab
 */
public class ServoController {

    private CommPortIdentifier portIdentifier;
    private CommPort port;
    private PrintWriter outputWriter;
    private BufferedReader reader;

    public void initSerialPort(String portName) throws NoSuchPortException, PortInUseException, IOException {
        portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        port = portIdentifier.open("JAVA-RTXT-PIX", 3000);
        outputWriter = new PrintWriter(port.getOutputStream());
    }

    public void setPosition(int p) {
        if (p < 500 || p > 2500) {
            throw new RuntimeException();
        }
        outputWriter.print("#1P" + p + "T100\r\n");
        outputWriter.flush();
    }

    public static void main(String[] args) throws Exception {
        ServoController servoController = new ServoController();
        servoController.initSerialPort("COM11");

        AdvantechADC advantechADC = new AdvantechADC();
        advantechADC.loadDevice();
        Device device = advantechADC.getDevice(0);
        device.open();

        servoController.setPosition(1500);
        Thread.sleep(1000);

        servoController.setPosition(1800);

        long start = System.nanoTime();
        File file = new File(start + ".csv");
        try (PrintWriter printWriter = new PrintWriter(file)) {
            long end = start + 1000000000l;
            while (true) {
                long time = System.nanoTime();
                if (time > end) {
                    break;
                }
                double v = device.readAIVoltage(0);
                printWriter.println(((time - start) / 1000000) + "," + v);
            }
        }
    }
}
