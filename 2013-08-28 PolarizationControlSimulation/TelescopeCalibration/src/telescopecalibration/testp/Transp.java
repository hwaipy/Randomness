package telescopecalibration.testp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Hwaipy
 */
public class Transp {

  private static final double STEP_LENGTH = 360.0 / 46752.0;
  private final Socket socket1;
  private final Socket socket2;
  private final OutputStream[] outs = new OutputStream[3];
  private final String[] iSs = new String[]{"1", "2", "1"};

  public Transp(String address1, String address2) throws IOException {
    socket1 = new Socket(address1, 23);
    socket2 = new Socket(address2, 23);
    outs[0] = socket1.getOutputStream();
    outs[1] = outs[0];
    outs[2] = socket2.getOutputStream();
  }

  public void set(int index, double degeree) throws IOException {
    OutputStream out = outs[index];
    int step = (int) (degeree / STEP_LENGTH);
    String cmd = iSs[index] + "PA" + step + "\n";
    out.write(cmd.getBytes());
  }
}
