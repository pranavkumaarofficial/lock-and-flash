package com.screentracker.app

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MonitoringService : Service() {
    
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var database: EventDatabase
    private lateinit var cameraManager: CameraManager
    private var torchCallback: CameraManager.TorchCallback? = null
    
    private val screenReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_SCREEN_ON -> {
                    logEvent(EventType.SCREEN_ON)
                }
                Intent.ACTION_USER_PRESENT -> {
                    logEvent(EventType.USER_PRESENT)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        database = EventDatabase.getInstance(this)
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        
        // Register screen receivers
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_USER_PRESENT)
        }
        registerReceiver(screenReceiver, filter)
        
        // Register flashlight callback
        torchCallback = object : CameraManager.TorchCallback() {
            override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
                super.onTorchModeChanged(cameraId, enabled)
                if (enabled) {
                    logEvent(EventType.FLASHLIGHT_ON)
                } else {
                    logEvent(EventType.FLASHLIGHT_OFF)
                }
            }
        }
        cameraManager.registerTorchCallback(torchCallback!!, null)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(screenReceiver)
        torchCallback?.let { cameraManager.unregisterTorchCallback(it) }
    }

    private fun logEvent(type: EventType) {
        serviceScope.launch {
            val event = Event(
                type = type,
                timestamp = System.currentTimeMillis()
            )
            database.eventDao().insertEvent(event)
        }
    }

    private fun createNotification(): Notification {
        createNotificationChannel()
        
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("ScreenTracker")
            .setContentText("Monitoring screen and flashlight events")
            .setSmallIcon(android.R.drawable.ic_menu_view)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Monitoring Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows when ScreenTracker is monitoring events"
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "ScreenTrackerChannel"
        private const val NOTIFICATION_ID = 1001
    }
}
