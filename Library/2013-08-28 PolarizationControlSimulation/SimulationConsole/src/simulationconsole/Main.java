package simulationconsole;

import java.io.IOException;
import java.util.Arrays;
import simulationconsole.entanglement.EntanglementSimulationConsole;

/**
 *
 * @author Hwaipy
 */
public class Main {

    public static void main(String[] args) throws IOException {
        if (args != null && args.length > 0 && "-e".equals(args[0])) {
            String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
//            System.out.println(Arrays.toString(newArgs));
            EntanglementSimulationConsole.main(newArgs);
        } else {
            SimulationSocket.main(args);
        }
    }
}
