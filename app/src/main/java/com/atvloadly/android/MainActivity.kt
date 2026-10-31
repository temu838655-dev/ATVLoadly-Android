package com.atvloadly.android

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(12, 18, 32)
        window.navigationBarColor = Color.rgb(12, 18, 32)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 32, 28, 32)
            setBackgroundColor(Color.rgb(12, 18, 32))
        }
        val title = TextView(this).apply {
            text = "ATVLoadly"
            textSize = 32f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }
        val subtitle = TextView(this).apply {
            text = "Apple TV · Wi-Fi IPA sideloading"
            textSize = 16f
            setTextColor(Color.rgb(170, 190, 220))
            gravity = Gravity.CENTER
            setPadding(0, 12, 0, 28)
        }
        val status = TextView(this).apply {
            text = "Android project initialized\n\nApple TV pairing and IPA installation are not implemented yet."
            textSize = 15f
            setTextColor(Color.rgb(230, 235, 245))
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 20)
        }
        val button = Button(this).apply {
            text = "Coming soon"
            isEnabled = false
        }
        root.addView(title, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        root.addView(subtitle, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        root.addView(status, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        root.addView(button, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        setContentView(root)
    }
}
