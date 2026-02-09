package edu.aitu.oop3.CoreComponent.config;

import java.util.List;

public class GymConfig {

    private static GymConfig instance;

    private final List<String> beginnerExercises = List.of(
            "Treadmill 10 min",
            "Bodyweight squats 3x12",
            "Push-ups 3x8",
            "Plank 3x30s"
    );

    private final List<String> premiumAddons = List.of(
            "Deadlift 5x5",
            "Bench press 5x5",
            "Pull-ups 3x6"
    );

    private GymConfig() {}

    public static GymConfig getInstance() {
        if (instance == null) instance = new GymConfig();
        return instance;
    }

    public List<String> beginnerExercises() {
        return beginnerExercises;
    }

    public List<String> premiumAddons() {
        return premiumAddons;
    }
}