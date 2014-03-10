/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package counterapplication.example;

/**
 *
 * @author Hwaipy
 */
public class Circle implements Shape {

    private double length;

    public Circle(double d) {
        length = d;
    }

    @Override
    public void setLength(double length) {
        this.length = length;
    }

    @Override
    public double getZC() {
        return Math.PI * length;
    }

    public double ImCircle() {
        return 47;
    }
}
