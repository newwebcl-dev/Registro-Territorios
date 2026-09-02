package cl.newweb.registroterritorios;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "registro_territorios_channel";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String titulo = "Registro Territorios";
        String mensaje = "Tienes una nueva notificación";

        if (remoteMessage.getNotification() != null) {

            if (remoteMessage.getNotification().getTitle() != null) {
                titulo = remoteMessage.getNotification().getTitle();
            }

            if (remoteMessage.getNotification().getBody() != null) {
                mensaje = remoteMessage.getNotification().getBody();
            }
        }

        mostrarNotificacion(titulo, mensaje);
    }

    private void mostrarNotificacion(String titulo, String mensaje) {

        crearCanalNotificaciones();

        Intent intent = new Intent(this, MainActivity.class);

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP
        );

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT |
                PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle(titulo)
                        .setContentText(mensaje)
                        .setStyle(
                                new NotificationCompat.BigTextStyle()
                                        .bigText(mensaje)
                        )
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(
                (int) System.currentTimeMillis(),
                builder.build()
        );
    }

    private void crearCanalNotificaciones() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "Notificaciones Registro Territorios",
                            NotificationManager.IMPORTANCE_HIGH
                    );

            channel.setDescription(
                    "Notificaciones de Registro Territorios"
            );

            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
}
