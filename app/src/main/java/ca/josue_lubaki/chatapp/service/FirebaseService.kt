package ca.josue_lubaki.chatapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import ca.josue_lubaki.chatapp.MainActivity
import ca.josue_lubaki.common.R
import ca.josue_lubaki.common.navigation.ScreenTarget
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

/**
 * created by Josue Lubaki
 * date : 2023-11-13
 * version : 1.0.0
 */

private const val CHANNEL_ID = "my_notification_channel"
class FirebaseService : FirebaseMessagingService() {

    companion object {
        var sharedPref:SharedPreferences? = null

        var token: String?
        get() {
            return sharedPref?.getString("token", "")
        }
        set(value) {
            sharedPref?.edit()?.putString("token", value)?.apply()
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        FirebaseInstallations.getInstance().getToken(true).addOnSuccessListener {
            sharedPref?.edit()?.putString("token", it.token)?.apply()
        }

        Log.d("xxxx", "onNewToken : $p0")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("route", ScreenTarget.Contact.route)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            /* id = */ CHANNEL_ID,
            /* name = */ "channelFirebase",
            /* importance = */ NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = " My Firebase chat description"
            enableLights(true)
            lightColor = Color.WHITE
        }
        notificationManager.createNotificationChannel(channel)
    }
}