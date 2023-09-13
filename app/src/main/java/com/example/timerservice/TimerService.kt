package com.example.timerservice

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.os.SystemClock
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class TimerService : Service() {

    private var isRunning = false
    private var startTimeMillis: Long = 0
    private var job: Job? = null
    private val broadcastIntent = Intent("TIMER_UPDATED")

    private lateinit var repositorioSeguimiento: RepositorioSeguimiento

    override fun onCreate() {
        super.onCreate()

        repositorioSeguimiento = SeguimientosLista()

        val filter = IntentFilter("RESET_TIMER")
        registerReceiver(resetReceiver, filter)

        val filter2 = IntentFilter("VIEW_LIST")
        registerReceiver(resetReceiver, filter2)
    }

    private val resetReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "RESET_TIMER") {
                // Restablece el cronómetro
                startTimeMillis = SystemClock.elapsedRealtime()
                saveTimeToDatabase()
            }

            if (intent?.action == "VIEW_LIST") {
                // Restablece el cronómetro
                for (i in 0 until repositorioSeguimiento.size()) {
                    println(repositorioSeguimiento.getById(i).toString())
                }
            }


        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isRunning) {
            startTimeMillis = SystemClock.elapsedRealtime()
            isRunning = true
            startTimer()
        }
        return START_STICKY
    }

    override fun onDestroy() {

        super.onDestroy()
        stopTimer()
        unregisterReceiver(resetReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startTimer() {
        job = CoroutineScope(Dispatchers.Default).launch {
            while (isRunning) {
                val elapsedTimeMillis = SystemClock.elapsedRealtime() - startTimeMillis
                val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMillis)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMillis) -
                        TimeUnit.MINUTES.toSeconds(minutes)

                // Actualiza la UI a través de una emisión (Intent)
                broadcastIntent.putExtra("minutes", minutes.toInt())
                broadcastIntent.putExtra("seconds", seconds.toInt())
                sendBroadcast(broadcastIntent)

                delay(1000) // Espera 1 segundo antes de la próxima actualización
            }
        }
    }

    private fun stopTimer() {

        isRunning = false
        job?.cancel()

    }

    private fun saveTimeToDatabase() {
        val currentTimeMillis = System.currentTimeMillis()

        repositorioSeguimiento.save(Seguimiento(-0.166093, currentTimeMillis.toDouble()))
        println("se agregó un elemnto")

    }


}

