package D20140710.FFT;

/**
 *
 * @author Hwaipy
 */
public class Complex {

    private final double re;
    private final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public Complex times(double t) {
        return new Complex(this.re * t, this.im * t);
    }

    public Complex times(Complex that) {
        return new Complex(this.re * that.re - this.im * that.im, this.re * that.im + this.im * that.re);
    }

    public Complex plus(Complex that) {
        return new Complex(this.re + that.re, this.im + that.im);
    }

    public Complex minus(Complex that) {
        return new Complex(this.re - that.re, this.im - that.im);
    }

    public Complex conjugate() {
        return new Complex(this.re, -this.im);
    }

    @Override
    public String toString() {
        if (im >= 0) {
            return "" + re + "+" + im + "i";
        } else {
            return "" + re + "" + im + "i";
        }
    }
}
