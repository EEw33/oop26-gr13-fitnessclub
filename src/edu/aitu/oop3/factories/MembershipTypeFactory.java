package edu.aitu.oop3.factories;

import edu.aitu.oop3.entities.MembershipType;

public class MembershipTypeFactory {
    public static MembershipType create(String type) {
        return switch (type.toUpperCase()) {
            case "BASIC" -> new MembershipType(1, "Basic", 30, 10000);
            case "PREMIUM" -> new MembershipType(2, "Premium", 90, 25000);
            case "STUDENT" -> new MembershipType(3, "Student", 30, 7000);
            default -> throw new IllegalArgumentException("Unknown membership type: " + type);
        };
    }
}
