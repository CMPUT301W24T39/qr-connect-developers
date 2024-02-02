package com.example.qrconnectdevelopers;

public abstract class Shape {
    protected int x;
    protected int y;

    protected String color = "purple"; // Default color is blue

    String color = "$PUT_YOUR_CHOICE_OF_COLOR";

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
