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
public class Squre implements Shape {

    private double length;

    public Squre(double l) {
        length = l;
    }

    @Override
    public void setLength(double length) {
        this.length = length;
    }

    @Override
    public double getZC() {
        return 4 * length;
    }

    public double ImSqure() {
        return 7;
    }

}
