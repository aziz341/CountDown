package com.example.testtimer

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import com.example.testtimer.databinding.ActivityMainBinding
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private var stopBtn : Button ?= null
    private var startBtn : Button ?= null
    private lateinit var binding : ActivityMainBinding
    private lateinit var serviceIntent : Intent
    private var countDownTimer :  CountDownTimer?=null
    private var bulb_on : ImageView ?= null
    private var bulb_off : ImageView ?= null
    private var player : MediaPlayer ?= null
    private var time = 0.0
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState )
        startBtn = findViewById(R.id.startBtn)
        stopBtn = findViewById(R.id.button2)



        bulb_on = findViewById(R.drawable.bulb_on)

        binding.startBtn.setOnClickListener {
            startedTimer()
        }
        binding.button2.setOnClickListener {
            stopedTimer()
        }
        serviceIntent = Intent(applicationContext,TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

            }
    private fun changeImage(){
        stopTimer()
        countDownTimer = 0.0
        binding.bulb_off = setImageView(R.drawable.bulb_on)
        player = MediaPlayer.create(this, R.raw.alarmbell)
    }

    private fun stopedTimer() {
        stopTimer()
    }

    private fun startedTimer() {
        if (countDownTimer)
            stopTimer()
        else
            stardTimer()
    }

    private fun stardTimer() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA,time)
        startService(serviceIntent)
        binding.button2.text = "Stop"

    }

    private fun stopTimer() {
        stopService(serviceIntent)
        binding.startBtn.text = "Start"

    }


    private val updateTime : BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, Intent: Intent?) {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA,0.0)
            binding.countDown.text = getTimeStringFromDouble(time)

        }
    }

    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val minutes = resultInt % 86488 % 3688 / 60
        val seconds = resultInt % 86488 % 3688 % 60
        return makeTimeString(minutes,seconds)


}

    private fun makeTimeString(minutes: Int, seconds: Int): String = String.format("%02d:%02d",minutes,seconds)




}