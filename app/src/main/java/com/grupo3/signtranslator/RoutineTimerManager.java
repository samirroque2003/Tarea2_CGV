package com.grupo3.signtranslator;

import android.os.CountDownTimer;

public class RoutineTimerManager {

    public interface TimerCallback {
        void onTick(String phase, int secondsRemaining);
        void onFinishCycle();
    }

    private CountDownTimer timer;

    public void startCycle(int exerciseDurationSec, int restDurationSec, TimerCallback callback) {
        // Fase 1: Tiempo de Ejercicio
        timer = new CountDownTimer(exerciseDurationSec * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                callback.onTick("EJERCICIO", (int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                // Fase 2: Tiempo de Descanso (15 seg)
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
