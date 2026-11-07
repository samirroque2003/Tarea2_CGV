package com.grupo3.signtranslator.models;

public class Exercise {
    private String name;
    private int durationSeconds;
    private String targetBodyPart; // Ej: "Pecho", "Pierna", "Espalda"

    public Exercise(String name, int durationSeconds, String targetBodyPart) {
        this.name = name;
        this.durationSeconds = durationSeconds;
        this.targetBodyPart = targetBodyPart;
    }

    public String getName() { return name; }
    public int getDurationSeconds() { return durationSeconds; }
    public String getTargetBodyPart() { return targetBodyPart; }
}