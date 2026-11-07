package com.grupo3.signtranslator;

import com.grupo3.signtranslator.models.Exercise;
import com.grupo3.signtranslator.models.RoutineDay;

import java.util.ArrayList;
import java.util.List;

public class RoutineRepository {
    public static RoutineDay getRoutineForDay(String dayName) {
        List<Exercise> exercises = new ArrayList<>();

        switch (dayName.toUpperCase()) {
            case "LUNES":
                exercises.add(new Exercise("Press de Banca", 45, "Pecho"));
                exercises.add(new Exercise("Flexiones", 30, "Pecho"));
                return new RoutineDay("LUNES", "Pecho", exercises);

            case "MARTES":
                exercises.add(new Exercise("Sentadillas", 50, "Piernas"));
                exercises.add(new Exercise("Zancadas", 40, "Piernas"));
                return new RoutineDay("MARTES", "Piernas", exercises);

            default:
                return new RoutineDay(dayName, "General", exercises);
        }
    }
}