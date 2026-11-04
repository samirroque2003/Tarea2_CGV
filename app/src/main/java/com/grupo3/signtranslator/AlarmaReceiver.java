package com.grupo3.signtranslator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmaReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "canal_ejercicios")
                .setSmallIcon(android.R.drawable.ic_menu_agenda)
                .setContentTitle("¡Hora de tu ronda de ejercicios!")
                .setContentText("Es momento de activar tu cuerpo y completar tu rutina.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        try {
            notificationManager.notify(1001, builder.build());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}