package com.example.qrconnectdevelopers;

public abstract class Shape {
    int x;
    int y;

<<<<<<< HEAD
    String color = "red";
=======
    String color = "white";
>>>>>>> dd25181af85b6416e785c8f0fcf74f3703cbfc04


    public Shape(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getColor() {
        return color;
    }

    public abstract double area();

    public abstract double perimeter();

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
