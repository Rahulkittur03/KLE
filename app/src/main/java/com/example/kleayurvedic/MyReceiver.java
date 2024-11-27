package com.example.kleayurvedic;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.kleayurvedic.Model.Schedule;
import com.example.kleayurvedic.utils.AndroidUtility;
import com.google.firebase.firestore.DocumentReference;

public class MyReceiver extends BroadcastReceiver {
    String Schedule_Status = "0";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("MyReceiver", "Received broadcast");
        String documentId = intent.getStringExtra("docId");
        String username = intent.getStringExtra("username");
        String OPDnumber = intent.getStringExtra("OPDNumber");
        Log.i("documentID", "" + documentId);

        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag");
        wakeLock.acquire();

        try {
            updateScheduleStatus(context, documentId);
        } catch (Exception e) {
            Log.i("Update", "not working");
        }

      //   ** Notification **

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Schedule_User_Date.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_FROM_BACKGROUND );
        final int ALARM_REQ =(int)System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQ, i, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        wakeLock.release();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Alarm_Manager")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Patient" + username)
                .setContentText("Patient Name :" + username + "with OPD Number" + OPDnumber)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.i("Rahul_notification","send");
        notificationManagerCompat.notify(ALARM_REQ, builder.build());
        }

    void updateScheduleStatus(Context context, String documentId) {
        // Update the Schedule_Status in Firestore
        DocumentReference documentReference = AndroidUtility.getCollectionReferenceSchedule().document(documentId);
        documentReference.update("schedule_Status", "0")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i("Update", "Status Updated");
                    } else {
                        Log.i("Update", "Failed to update status");
                    }
                });
    }
}
