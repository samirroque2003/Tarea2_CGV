package com.grupo3.signtranslator;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class RoutineScheduleActivity extends AppCompatActivity {

    // Componentes de la UI (asegúrate de que coincidan con los IDs de tu archivo XML)
    private TextView txtDayInfo, txtPhaseInfo, txtTimerDisplay;
    private Button btnStartExercise;

    private RoutineTimerManager timerManager;
    private RoutineDay currentRoutineDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_schedule); // Mantén tu layout original intacto

        // Inicializar vistas (opcional según tu diseño actual)
        // txtDayInfo = findViewById(R.id.txtDayInfo);
        // txtPhaseInfo = findViewById(R.id.txtPhaseInfo);
        // txtTimerDisplay = findViewById(R.id.txtTimerDisplay);
        // btnStartExercise = findViewById(R.id.btnStartExercise);

        timerManager = new RoutineTimerManager();

        // MEJORA 1: Cargar la sugerencia de rutina basada en el día actual (ej: "LUNES")
        // Puedes cambiar esto dinámicamente según Calendar.getInstance()
        currentRoutineDay = RoutineRepository.getRoutineForDay("LUNES");

        // Ejemplo de ejecución al presionar un botón o iniciar la actividad
        if (!currentRoutineDay.getExercises().isEmpty()) {
            Exercise ejercicioPrueba = currentRoutineDay.getExercises().get(0);

            // Validar e iniciar directamente de manera segura
            ejecutarEjercicioConValidacion(ejercicioPrueba);
        }
    }

    /**
     * Aplica la MEJORA 2: Valida la parte del cuerpo antes de permitir la ejecución.
     */
    private void ejecutarEjercicioConValidacion(Exercise ejercicioSeleccionado) {
        String errorRestriccion = RoutineValidator.validateExercise(currentRoutineDay, ejercicioSeleccionado);

        if (errorRestriccion != null) {
            // Restricción activada: bloquea el flujo si no corresponde al día
            Toast.makeText(this, errorRestriccion, Toast.LENGTH_LONG).show();
            return;
        }

        // Si pasa la validación, iniciamos el temporizador con 15 segundos de descanso (MEJORA 3)
        Toast.makeText(this, "Iniciando: " + ejercicioSeleccionado.getName(), Toast.LENGTH_SHORT).show();

        timerManager.startCycle(ejercicioSeleccionado.getDurationSeconds(), 15, new RoutineTimerManager.TimerCallback() {
            @Override
            public void onTick(String phase, int secondsRemaining) {
                // Aquí puedes actualizar los TextViews de tu interfaz en tiempo real
                // Ej: txtPhaseInfo.setText("Fase: " + phase);
                //     txtTimerDisplay.setText(secondsRemaining + "s");
            }

            @Override
            public void onFinishCycle() {
                Toast.makeText(RoutineScheduleActivity.this, "¡Rutina y descansos finalizados con éxito!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Limpieza de recursos para evitar fugas de memoria (memory leaks)
        if (timerManager != null) {
            timerManager.cancel();
        }
    }

    // ==========================================
    // CLASES DE APOYO (Modelos, Repositorio y Lógica)
    // ==========================================

    public static class Exercise {
        private String name;
        private int durationSeconds;
        private String targetBodyPart; // Ej: "Pecho", "Piernas"

        public Exercise(String name, int durationSeconds, String targetBodyPart) {
            this.name = name;
            this.durationSeconds = durationSeconds;
            this.targetBodyPart = targetBodyPart;
        }

        public String getName() { return name; }
        public int getDurationSeconds() { return durationSeconds; }
        public String getTargetBodyPart() { return targetBodyPart; }
    }

    public static class RoutineDay {
        private String dayOfWeek;
        private String targetMuscleGroup;
        private List<Exercise> exercises;

        public RoutineDay(String dayOfWeek, String targetMuscleGroup, List<Exercise> exercises) {
            this.dayOfWeek = dayOfWeek;
            this.targetMuscleGroup = targetMuscleGroup;
            this.exercises = exercises;
        }

        public String getTargetMuscleGroup() { return targetMuscleGroup; }
        public List<Exercise> getExercises() { return exercises; }
    }

    public static class RoutineRepository {
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

    public static class RoutineValidator {
        public static String validateExercise(RoutineDay todayRoutine, Exercise selectedExercise) {
            if (todayRoutine == null || todayRoutine.getTargetMuscleGroup() == null) {
                return "No hay rutina asignada para el día de hoy.";
            }

            boolean isValid = todayRoutine.getTargetMuscleGroup()
                    .equalsIgnoreCase(selectedExercise.getTargetBodyPart());

            if (!isValid) {
                return "⚠️ Restricción: Hoy corresponde trabajar "
                        + todayRoutine.getTargetMuscleGroup()
                        + ". El ejercicio seleccionado es de "
                        + selectedExercise.getTargetBodyPart() + ".";
            }
            return null; // Aprobado
        }
    }

    public static class RoutineTimerManager {
        private CountDownTimer timer;

        public interface TimerCallback {
            void onTick(String phase, int secondsRemaining);
            void onFinishCycle();
        }

        public void startCycle(int exerciseDurationSec, int restDurationSec, TimerCallback callback) {
            // Fase 1: Tiempo de Ejercicio Activo
            timer = new CountDownTimer(exerciseDurationSec * 1000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    callback.onTick("EJERCICIO", (int) (millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    // Fase 2: Tiempo de Descanso Obligatorio (15 segundos)
                    startRestPhase(restDurationSec, callback);
                }
            }.start();
        }

        private void startRestPhase(int restDurationSec, TimerCallback callback) {
            timer = new CountDownTimer(restDurationSec * 1000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    callback.onTick("DESCANSO", (int) (millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    callback.onFinishCycle();
                }
            }.start();
        }

        public void cancel() {
            if (timer != null) {
                timer.cancel();
            }
        }
    }
}