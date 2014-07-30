package D20140729.OAMDrawer;

import org.jscience.mathematics.number.Complex;

/**
 *
 * @author Hwaipy
 */
public class Sum implements Function {

    private final Function function1;
    private final Function function2;

    public Sum(Function function1, Function function2) {
        this.function1 = function1;
        this.function2 = function2;
    }

    @Override
    public Complex valueOf(double x, double y) {
        return function1.valueOf(x, y).plus(function2.valueOf(x, y));
    }
}
