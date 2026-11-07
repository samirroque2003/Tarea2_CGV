package com.grupo3.signtranslator.models;

import java.util.List;

public class RoutineDay {
    private String dayOfWeek; // Ej: "LUNES"
    private String targetMuscleGroup; // Ej: "Pecho"
    private List<Exercise> exercises;

    public RoutineDay(String dayOfWeek, String targetMuscleGroup, List<Exercise> exercises) {
        this.dayOfWeek = dayOfWeek;
        this.targetMuscleGroup = targetMuscleGroup;
        this.exercises = exercises;
    }

    public String getDayOfWeek() { return dayOfWeek; }
    public String getTargetMuscleGroup() { return targetMuscleGroup; }
    public List<Exercise> getExercises() { return exercises; }
}