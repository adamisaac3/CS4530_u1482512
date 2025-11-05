package com.example.marblegame.viewmodel

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.marblegame.model.MarbleState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.max
import kotlin.math.min

class MarbleViewModel(application: Application): AndroidViewModel(application), SensorEventListener, DefaultLifecycleObserver {
    private val sensorManager = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)

    private val _marbleState = MutableStateFlow(MarbleState())
    val marbleState: StateFlow<MarbleState> = _marbleState.asStateFlow()

    private var velocityX = 0f
    private var velocityY = 0f
    private var positionX = 0f
    private var positionY = 0f

    private var maxWidthPx = 1080f
    private var maxHeightPx = 1920f

    private val marbleSizePx = 40f * 3f
    private var density = 3f

    private val scale = 50f
    private val friction = 0.98f
    private var lastTimestamp = 0L
    private var isInitialized = false

    init{
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onResume(owner: LifecycleOwner){
        super.onResume(owner)
        gravitySensor?.let{
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        sensorManager.unregisterListener(this)
    }

    fun updateScreenBounds(widthPx: Float, heightPx: Float){
        if (!isInitialized || maxWidthPx != widthPx || maxHeightPx != heightPx) {
            maxWidthPx = widthPx
            maxHeightPx = heightPx

            // Calculate density for px to dp conversion
            density = widthPx / (widthPx / 3f) // Rough estimate, will work

            if (!isInitialized) {
                // Initialize marble to center (in dp)
                positionX = (widthPx / density / 2f) - 20f
                positionY = (heightPx / density / 2f) - 20f
                _marbleState.value = MarbleState(
                    x = positionX,
                    y = positionY
                )
                isInitialized = true
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type == Sensor.TYPE_GRAVITY){
            val gravityX = event.values[0]
            val gravityY = event.values[1]

            val currentTime = System.nanoTime()
            val dt = if (lastTimestamp != 0L){
                (currentTime - lastTimestamp) / 1_000_000_000f
            }else{
                0.016f
            }
            lastTimestamp = currentTime

            velocityX += dt * scale * -gravityX
            velocityY += dt * scale * gravityY

            velocityX *= friction
            velocityY *= friction

            positionX += dt * scale * velocityX
            positionY += dt * scale * velocityY

            val maxX = (maxWidthPx / density) - 40f
            val maxY = (maxHeightPx / density) - 40f

            positionX = max(0f, min(positionX, maxX))
            positionY = max(0f, min(positionY, maxY))

            if(positionX <= 0f || positionX >= maxX){
                velocityX *= -.5f
            }
            if(positionY <= 0f || positionY >= maxY){
                velocityY *= -.5f
            }

            _marbleState.value = MarbleState(
                x = positionX,
                y = positionY
            )
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //Not needed
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
    }

}