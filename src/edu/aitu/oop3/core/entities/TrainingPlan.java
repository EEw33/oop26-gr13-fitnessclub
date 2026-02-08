package edu.aitu.oop3.core.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrainingPlan {

    private final long memberId;
    private final String title;
    private final List<String> exercises;

    private TrainingPlan(Builder b) {
        this.memberId = b.memberId;
        this.title = b.title;
        this.exercises = List.copyOf(b.exercises);
    }

    public long getMemberId() { return memberId; }
    public String getTitle() { return title; }
    public List<String> getExercises() { return Collections.unmodifiableList(exercises); }

    public static class Builder {
        private long memberId;
        private String title;
        private final List<String> exercises = new ArrayList<>();

        public Builder memberId(long memberId) {
            this.memberId = memberId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder addExercise(String ex) {
            this.exercises.add(ex);
            return this;
        }

        public Builder addExercises(List<String> exs) {
            this.exercises.addAll(exs);
            return this;
        }

        public TrainingPlan build() {
            if (memberId <= 0) throw new IllegalStateException("memberId required");
            if (title == null || title.isBlank()) throw new IllegalStateException("title required");
            if (exercises.isEmpty()) throw new IllegalStateException("at least one exercise required");
            return new TrainingPlan(this);
        }
    }
}
