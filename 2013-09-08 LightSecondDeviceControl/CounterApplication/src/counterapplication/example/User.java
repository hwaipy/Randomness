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
public class User {

    public static void main(String[] args) {
        Shape shape1 = new Circle(100);
        Shape shape2 = new Squre(100);
        System.out.println(shape1.getZC());
        System.out.println(shape2.getZC());
        Circle c1 = (Circle) shape1;
        Circle c2 = (Circle) shape2;
    }
}
