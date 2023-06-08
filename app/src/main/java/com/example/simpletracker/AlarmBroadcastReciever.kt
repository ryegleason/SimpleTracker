package com.example.simpletracker

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import java.util.*
import kotlin.math.max

const val MS_IN_HOUR : Long = 3600000L
class AlarmBroadcastReciever : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("simpletracker", "alarm went off")
        // Create an explicit intent for an Activity in your app
        val surveyIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, surveyIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_ballot_24)
            .setContentTitle("Survey Time")
            .setContentText("click me to take the survey")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val pebbleIntent = Intent("com.getpebble.action.SEND_NOTIFICATION")
        pebbleIntent.putExtra("messageType", "PEBBLE_ALERT")
        pebbleIntent.putExtra("notificationData", "[{\"title\":\"Survey\",\"body\":\"Survey time!\"}]")
        context.sendBroadcast(pebbleIntent);

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define

            if (areNotificationsEnabled()) {
                notify(1, builder.build())
            } else {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.w("simpletracker", "No notification permissions :(")
                return
            }
        }

    }
}

fun setAlarm(activity: Activity, isWakeup: Boolean) {
    val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(activity, AlarmBroadcastReciever::class.java)
    intent.action = "com.example.simpletracker.ALARM_BROADCAST"
    intent.flags = Intent.FLAG_INCLUDE_STOPPED_PACKAGES
    val pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, PendingIntent.FLAG_IMMUTABLE)


    val random = Random()
    val timeUntilAlarm: Long = if (isWakeup) {
        ((0.25 + random.nextDouble() * 1.75) * MS_IN_HOUR).toLong()
    } else {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val period: Long = sharedPreferences.getString("ask_period", "4")!!.toLong() * MS_IN_HOUR
        max(MS_IN_HOUR / 6L, period + (random.nextGaussian() / 3.0 * period).toLong())
    }

    alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeUntilAlarm, pendingIntent)
    Log.d("simpletracker", "alarm set")
}