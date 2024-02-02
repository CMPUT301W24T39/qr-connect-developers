package com.example.qrconnectdevelopers;

public abstract class Shape {
    int x;
    int y;

<<<<<<< HEAD
    protected String color = "purple"; // Default color is blue

    String color = "dark";
=======
    String color = "pink"; 
>>>>>>> 4435e2d (Updated color from my branch.)

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
