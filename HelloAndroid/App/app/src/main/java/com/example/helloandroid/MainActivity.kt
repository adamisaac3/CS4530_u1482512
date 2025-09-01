package com.example.helloandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.text.format

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnMap = hashMapOf(
            R.id.button1 to "#fc0303",
            R.id.button2 to "#03e8fc",
            R.id.button3 to "#1f661e",
            R.id.button4 to "#F8DE11",
            R.id.button5 to "#8e918e"
        )

        for(id in btnMap.keys){
            val btn = findViewById<Button>(id)

            setOnClickListener(btn, btnMap[id])
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    /**
     * For each of the 5 buttons we want to set the same on click listener
     */
    private fun setOnClickListener(btn: android.widget.Button, hex: String?){
        val hexColor = hex ?: "#FFFFFF"
        btn.setOnClickListener{
            onBtnClick(text=btn.text, colorHex=hexColor)
        }
    }

    /**
     * For any button click we want to go the second activity.
     * Button kkey contains the text, while the color has the color hex for the second screen background
     */
    private fun onBtnClick(text: CharSequence, colorHex: String){
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("BUTTON", text)
        intent.putExtra("COLOR", colorHex)
        startActivity(intent)
    }

}