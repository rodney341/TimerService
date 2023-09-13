package com.example.timerservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var btnStartStop: Button
    private lateinit var btnReset: Button
    private lateinit var btnList: Button
    private lateinit var tvElapsedTime: TextView
    private var isRunning = false
    private lateinit var timerServiceIntent: Intent
    private var isServiceBound = false
    private var job: Job? = null // Mantener una referencia al trabajo de la corutina

    private val timerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "TIMER_UPDATED") {
                val minutes = intent.getIntExtra("minutes", 0)
                val seconds = intent.getIntExtra("seconds", 0)
                val elapsedTime = String.format("%02d:%02d", minutes, seconds)
                tvElapsedTime.text = "Tiempo transcurrido: $elapsedTime"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStartStop = findViewById(R.id.btnStartStop)
        btnReset = findViewById(R.id.btnReset)
        btnList = findViewById(R.id.btnList)
        tvElapsedTime = findViewById(R.id.tvElapsedTime)

        timerServiceIntent = Intent(this, TimerService::class.java)

        btnStartStop.setOnClickListener(View.OnClickListener {
            if (isRunning) {
                stopTimerService()
            } else {
                startTimerService()
            }
        })

        btnReset.setOnClickListener(View.OnClickListener {
            resetTimer()
        })

        btnList.setOnClickListener(View.OnClickListener {
            viewList()
        })

        val filter = IntentFilter("TIMER_UPDATED")
        registerReceiver(timerReceiver, filter)
    }

    private fun startTimerService() {
        startService(timerServiceIntent)
        btnStartStop.text = "Detener"
        isRunning = true

        // Iniciar una corutina para actualizar la UI periódicamente
        job = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                delay(1000) // Esperar 1 segundo
                // Aquí puedes realizar otras tareas en segundo plano si es necesario
            }
        }
    }

    private fun stopTimerService() {
        stopService(timerServiceIntent)
        btnStartStop.text = "Iniciar"
        isRunning = false

        // Cancelar la corutina cuando se detiene el servicio
        job?.cancel()
    }

    private fun resetTimer() {
        val intent = Intent("RESET_TIMER")
        sendBroadcast(intent)
    }

    private fun viewList() {
        val intent = Intent("VIEW_LIST")
        sendBroadcast(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(timerReceiver)
        job?.cancel() // Asegurarse de cancelar la corutina al destruir la actividad
    }

}
