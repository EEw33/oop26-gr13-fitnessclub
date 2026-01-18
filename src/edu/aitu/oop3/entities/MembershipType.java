package edu.aitu.oop3.entities;

public class MembershipType {
    private int id;
    private String name;        // Basic, Premium, Student
    private int durationDays;   // срок действия
    private double price;

    public MembershipType(int id, String name, int durationDays, double price) {
        this.id = id;
        this.name = name;
        this.durationDays = durationDays;
        this.price = price;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getDurationDays() {
        return durationDays;
    }
    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}
