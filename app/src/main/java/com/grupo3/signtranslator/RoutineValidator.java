package com.grupo3.signtranslator;

import com.grupo3.signtranslator.models.Exercise;
import com.grupo3.signtranslator.models.RoutineDay;

public class RoutineValidator {
    /**
     * Retorna null si la rutina es válida, o un mensaje de error si no coincide.
     */
    public static String validateExercise(RoutineDay todayRoutine, Exercise selectedExercise) {
        if (todayRoutine == null || todayRoutine.getTargetMuscleGroup() == null) {
            return "No hay rutina asignada para el día de hoy.";
        }

        boolean isValid = todayRoutine.getTargetMuscleGroup().equalsIgnoreCase(selectedExercise.getTargetBodyPart());

        if (!isValid) {
            return "⚠️ Restricción: Hoy corresponde trabajar "
                    + todayRoutine.getTargetMuscleGroup()
                    + ". El ejercicio seleccionado es de "
                    + selectedExercise.getTargetBodyPart() + ".";
        }
        return null; // Válido
    }
}