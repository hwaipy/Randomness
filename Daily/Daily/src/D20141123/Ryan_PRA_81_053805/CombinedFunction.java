package D20141123.Ryan_PRA_81_053805;

/**
 *
 * @author Hwaipy
 */
public class CombinedFunction implements Function {

    public static final int PLUS = 0;
    public static final int MINUS = 1;
    public static final int TIMES = 2;
    public static final int DIVIDE = 3;
    private final Function function2;
    private final int operator;
    private final Function function1;

    public CombinedFunction(Function function1, Function function2, int operator) {
        this.function1 = function1;
        this.function2 = function2;
        this.operator = operator;
    }

    @Override
    public double value(double x) {
        switch (operator) {
            case PLUS:
                return function1.value(x) + function2.value(x);
            case MINUS:
                return function1.value(x) - function2.value(x);
            case TIMES:
                return function1.value(x) * function2.value(x);
            case DIVIDE:
                return function1.value(x) / function2.value(x);
            default:
                throw new RuntimeException();
        }
    }
}
