package com.example.helloandroid

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.graphics.toColorInt

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)

        val returnBtn = findViewById<Button>(R.id.returnBtn)
        val btnText = findViewById<TextView>(R.id.textView)


        val mainLayout = findViewById<View>(R.id.main)


        //Get the passed text and set the text view to it.
        btnText.text = intent.getStringExtra("BUTTON")

        //Set the main layout background color to the passed hex from the main activity
        mainLayout.setBackgroundColor(intent.getStringExtra("COLOR")!!.toColorInt())


        returnBtn.setOnClickListener {
            onReturnClick()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun onReturnClick(){

        //Just go back to the main activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}