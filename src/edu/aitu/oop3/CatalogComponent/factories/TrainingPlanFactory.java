package edu.aitu.oop3.CatalogComponent.factories;

import edu.aitu.oop3.CoreComponent.config.GymConfig;
import edu.aitu.oop3.MemberManagmentComponent.entities.Member;
import edu.aitu.oop3.CatalogComponent.entities.TrainingPlan;

import java.util.List;
import java.util.function.Predicate;

public class TrainingPlanFactory {

    public static TrainingPlan createFor(Member member) {
        GymConfig cfg = GymConfig.getInstance();

        // Lambda / Predicate requirement: filter exercises
        Predicate<String> safeForBeginners = ex ->
                !ex.toLowerCase().contains("deadlift") &&
                        !ex.toLowerCase().contains("bench") &&
                        !ex.toLowerCase().contains("pull-ups");

        long typeId = member.getMembershipTypeId() == null ? 1 : member.getMembershipTypeId();

        // Factory makes different plan variants based on membership type
        return switch ((int) typeId) {
            case 2 -> { // Premium
                TrainingPlan plan = new TrainingPlan.Builder()
                        .memberId(member.getId())
                        .title("Premium strength plan")
                        .addExercises(cfg.beginnerExercises())
                        .addExercises(cfg.premiumAddons())
                        .build();
                yield plan;
            }
            case 3 -> { // Student
                List<String> filtered = cfg.beginnerExercises().stream()
                        .filter(safeForBeginners)
                        .toList();

                yield new TrainingPlan.Builder()
                        .memberId(member.getId())
                        .title("Student starter plan")
                        .addExercises(filtered)
                        .build();
            }
            default -> { // Basic
                yield new TrainingPlan.Builder()
                        .memberId(member.getId())
                        .title("Basic beginner plan")
                        .addExercises(cfg.beginnerExercises())
                        .build();
            }
        };
    }
}