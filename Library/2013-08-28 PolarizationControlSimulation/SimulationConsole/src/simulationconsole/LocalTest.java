package simulationconsole;

import java.io.IOException;
import simulationconsole.entanglement.EntanglementSimulationConsole;

/**
 *
 * @author Hwaipy
 */
public class LocalTest {

    public static void main(String[] args) throws IOException {
        EntanglementSimulationConsole.main(new String[]{"-r", "1"});
    }
}
