package com.example.qrconnectdevelopers;

public abstract class Shape {
    int x;
    int y;
<<<<<<< HEAD

    String color = "blue";
    String color = "red";
=======

>>>>>>> 818fa89c8810c85834e1673b973a32da9d93b64a

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
